package com.vg.subtitle.extractor

import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import com.vg.subtitle.api.CorruptedVideoException
import com.vg.subtitle.api.SubtitleProgress
import com.vg.subtitle.api.UnsupportedCodecException
import com.vg.subtitle.utils.TimeUtils
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.roundToInt

/** Refines audio extraction with normalization and speech enhancement for better recognition. */
class VideoAudioExtractor(private val decoder: AudioDecoder = AudioDecoder()) {
    fun extractToWav(video: File, outputWav: File, onProgress: (SubtitleProgress) -> Unit = {}): File {
        val durationMs = durationMs(video)
        val started = System.currentTimeMillis()
        
        // Step 1: Decode to a temporary PCM file and find the peak amplitude for normalization
        val tempRaw = File.createTempFile("audio_raw_", ".pcm", video.parentFile)
        var maxAbs = 0
        try {
            RandomAccessFile(tempRaw, "rw").use { raf ->
                decoder.decodeToPcm16kMono(video) { progressUs ->
                    val processedMs = progressUs / 1_000L
                    onProgress(
                        SubtitleProgress(
                            percent = (TimeUtils.percent(processedMs, durationMs) * 0.45).toInt(),
                            processedMs = processedMs,
                            totalMs = durationMs,
                            currentTimestampMs = processedMs,
                            etaMs = TimeUtils.estimateEta(processedMs, durationMs, System.currentTimeMillis() - started) * 2,
                            message = "Analyzing audio",
                        ),
                    )
                }.forEach { chunk ->
                    raf.write(chunk)
                    val buffer = ByteBuffer.wrap(chunk).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                    while (buffer.hasRemaining()) {
                        val abs = Math.abs(buffer.get().toInt())
                        if (abs > maxAbs) maxAbs = abs
                    }
                }
            }

            // Step 2: Normalize and apply speech enhancement (High-pass + Pre-emphasis)
            val targetPeak = 28000f // Leave some headroom for filter overshoot
            val gain = if (maxAbs > 0) targetPeak / maxAbs else 1.0f
            val refiner = SpeechRefiner()
            val wavWriter = WAVWriter(outputWav)
            
            FileInputStream(tempRaw).use { fis ->
                val buffer = ByteArray(128 * 1024)
                var bytesRead: Int
                var totalRead = 0L
                val totalBytes = tempRaw.length()
                
                wavWriter.writeWithAction { wav ->
                    while (fis.read(buffer).also { bytesRead = it } != -1) {
                        val chunk = if (bytesRead == buffer.size) buffer else buffer.copyOf(bytesRead)
                        val shorts = ByteBuffer.wrap(chunk).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                        val shortArray = ShortArray(shorts.limit())
                        shorts.get(shortArray)
                        
                        val refined = refiner.refine(shortArray, gain)
                        val outputBytes = refined.toLittleEndianBytes()
                        wav.write(outputBytes)
                        
                        totalRead += bytesRead
                        val progressPercent = 45 + (totalRead * 55 / totalBytes.coerceAtLeast(1)).toInt()
                        onProgress(
                            SubtitleProgress(
                                percent = progressPercent.coerceIn(45, 100),
                                processedMs = durationMs,
                                totalMs = durationMs,
                                currentTimestampMs = durationMs,
                                etaMs = 0,
                                message = if (progressPercent < 100) "Enhancing speech" else "Finalizing WAV",
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            throw CorruptedVideoException("Failed to refine audio: ${e.message}", e)
        } finally {
            if (tempRaw.exists()) tempRaw.delete()
        }
        return outputWav
    }

    fun durationMs(video: File): Long {
        val extractor = MediaExtractor()
        return try {
            extractor.setDataSource(video.absolutePath)
            for (index in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(index)
                if (format.containsKey(MediaFormat.KEY_DURATION)) {
                    return format.getLong(MediaFormat.KEY_DURATION) / 1_000L
                }
            }
            0L
        } catch (e: Exception) {
            0L
        } finally {
            extractor.release()
        }
    }
}

class AudioDecoder {
    fun decodeToPcm16kMono(input: File, onProgressUs: (Long) -> Unit = {}): Sequence<ByteArray> = sequence {
        val extractor = MediaExtractor()
        var codec: MediaCodec? = null
        try {
            extractor.setDataSource(input.absolutePath)
            val trackIndex = findAudioTrack(extractor)
            extractor.selectTrack(trackIndex)
            val format = extractor.getTrackFormat(trackIndex)
            val mime = format.getString(MediaFormat.KEY_MIME)
                ?: throw UnsupportedCodecException("Audio track has no MIME type.")
            
            codec = MediaCodec.createDecoderByType(mime)
            // Request 16-bit PCM explicitly
            format.setInteger(MediaFormat.KEY_PCM_ENCODING, AudioFormat.ENCODING_PCM_16BIT)
            codec.configure(format, null, null, 0)
            codec.start()
            
            val converter = PCMConverter()
            val info = MediaCodec.BufferInfo()
            val inputSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            val inputChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
            var sawInputEnd = false
            var sawOutputEnd = false

            while (!sawOutputEnd) {
                if (!sawInputEnd) {
                    val inputIndex = codec.dequeueInputBuffer(TIMEOUT_US)
                    if (inputIndex >= 0) {
                        val inputBuffer = codec.getInputBuffer(inputIndex)
                        inputBuffer?.clear()
                        val sampleSize = extractor.readSampleData(inputBuffer ?: continue, 0)
                        if (sampleSize < 0) {
                            codec.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            sawInputEnd = true
                        } else {
                            codec.queueInputBuffer(inputIndex, 0, sampleSize, extractor.sampleTime, 0)
                            onProgressUs(extractor.sampleTime.coerceAtLeast(0L))
                            extractor.advance()
                        }
                    }
                }
                when (val outputIndex = codec.dequeueOutputBuffer(info, TIMEOUT_US)) {
                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> Unit
                    MediaCodec.INFO_TRY_AGAIN_LATER -> Unit
                    else -> if (outputIndex >= 0) {
                        val outputBuffer = codec.getOutputBuffer(outputIndex)
                        if (outputBuffer != null && info.size > 0) {
                            outputBuffer.position(info.offset)
                            outputBuffer.limit(info.offset + info.size)
                            val pcm = ByteArray(info.size)
                            outputBuffer.get(pcm)
                            yield(converter.toMono16kPcm16(pcm, inputSampleRate, inputChannels))
                        }
                        sawOutputEnd = info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0
                        codec.releaseOutputBuffer(outputIndex, false)
                    }
                }
            }
        } catch (error: Exception) {
            throw CorruptedVideoException("Unable to decode audio: ${error.message}", error)
        } finally {
            codec?.runCatching { stop() }
            codec?.release()
            extractor.release()
        }
    }

    private fun findAudioTrack(extractor: MediaExtractor): Int {
        for (index in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(index)
            val mime = format.getString(MediaFormat.KEY_MIME).orEmpty()
            if (mime.startsWith("audio/")) return index
        }
        throw UnsupportedCodecException("No supported audio track was found.")
    }

    private companion object {
        const val TIMEOUT_US = 10_000L
    }
}

class PCMConverter {
    fun toMono16kPcm16(input: ByteArray, inputSampleRate: Int, inputChannels: Int): ByteArray {
        if (input.isEmpty()) return input
        val mono = toMono(input, inputChannels.coerceAtLeast(1))
        return resampleLinear(mono, inputSampleRate.coerceAtLeast(1), 16_000).toLittleEndianBytes()
    }

    private fun toMono(input: ByteArray, channels: Int): ShortArray {
        val shorts = ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        val frames = shorts.limit() / channels
        val mono = ShortArray(frames)
        for (frame in 0 until frames) {
            var sum = 0L
            for (channel in 0 until channels) {
                val idx = frame * channels + channel
                if (idx < shorts.limit()) {
                    sum += shorts.get(idx).toLong()
                }
            }
            mono[frame] = (sum / channels).toShort()
        }
        return mono
    }

    private fun resampleLinear(input: ShortArray, inputRate: Int, outputRate: Int): ShortArray {
        if (input.isEmpty()) return ShortArray(0)
        if (inputRate == outputRate) return input
        val outputSize = (input.size.toLong() * outputRate / inputRate).toInt().coerceAtLeast(1)
        val output = ShortArray(outputSize)
        val ratio = inputRate.toDouble() / outputRate.toDouble()
        for (index in output.indices) {
            val position = index * ratio
            val low = position.toInt().coerceIn(0, input.lastIndex)
            val high = (low + 1).coerceAtMost(input.lastIndex)
            val fraction = position - low
            output[index] = (input[low] + (input[high] - input[low]) * fraction).roundToInt().toShort()
        }
        return output
    }
}

/** Applies high-pass filtering and pre-emphasis to make speech more distinct for the AI engine. */
class SpeechRefiner {
    private var lastSample = 0f
    private var hpLastInput = 0f
    private var hpLastOutput = 0f
    private val hpAlpha = 0.98f // Cutoff ~100Hz at 16kHz to remove DC offset and low rumble

    fun refine(samples: ShortArray, gain: Float): ShortArray {
        val output = ShortArray(samples.size)
        for (i in samples.indices) {
            val s = samples[i].toFloat()
            
            // 1. High-pass filter
            val filtered = hpAlpha * (hpLastOutput + s - hpLastInput)
            hpLastInput = s
            hpLastOutput = filtered
            
            // 2. Normalization & Gain
            val normalized = filtered * gain
            
            // 3. Pre-emphasis (High-frequency boost for consonant clarity)
            val pre = normalized - 0.95f * lastSample
            lastSample = normalized
            
            output[i] = pre.roundToInt().toShort().coerceIn(-32768, 32767)
        }
        return output
    }
}

fun ShortArray.toLittleEndianBytes(): ByteArray {
    val buffer = ByteBuffer.allocate(size * 2).order(ByteOrder.LITTLE_ENDIAN)
    forEach { buffer.putShort(it) }
    return buffer.array()
}

class WAVWriter(
    private val output: File,
    private val sampleRateHz: Int = 16_000,
    private val channels: Int = 1,
    private val bitsPerSample: Int = 16,
) {
    fun writeWithAction(action: (RandomAccessFile) -> Unit): File {
        output.parentFile?.mkdirs()
        RandomAccessFile(output, "rw").use { wav ->
            wav.setLength(0L)
            writeHeader(wav, 0L)
            action(wav)
            val dataSize = wav.length() - 44
            wav.seek(0L)
            writeHeader(wav, dataSize)
        }
        return output
    }

    private fun writeHeader(file: RandomAccessFile, pcmDataBytes: Long) {
        val byteRate = sampleRateHz * channels * bitsPerSample / 8
        val blockAlign = channels * bitsPerSample / 8
        file.writeBytes("RIFF")
        file.writeIntLE((36L + pcmDataBytes).toInt())
        file.writeBytes("WAVE")
        file.writeBytes("fmt ")
        file.writeIntLE(16)
        file.writeShortLE(1)
        file.writeShortLE(channels)
        file.writeIntLE(sampleRateHz)
        file.writeIntLE(byteRate)
        file.writeShortLE(blockAlign)
        file.writeShortLE(bitsPerSample)
        file.writeBytes("data")
        file.writeIntLE(pcmDataBytes.toInt())
    }
}

private fun RandomAccessFile.writeIntLE(value: Int) {
    write(value and 0xff)
    write(value shr 8 and 0xff)
    write(value shr 16 and 0xff)
    write(value shr 24 and 0xff)
}

private fun RandomAccessFile.writeShortLE(value: Int) {
    write(value and 0xff)
    write(value shr 8 and 0xff)
}
