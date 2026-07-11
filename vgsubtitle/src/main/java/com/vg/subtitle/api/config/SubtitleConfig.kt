package com.vg.subtitle.api.config

import androidx.annotation.Keep
import com.vg.subtitle.api.model.SubtitleFormat
import com.vg.subtitle.api.model.WhisperModel
import java.util.Locale

/**
 * Configuration for subtitle generation, translation, and optional translated audio (dubbing).
 *
 * This data class holds all parameters that control how the [VGSubtitleEngine] processes
 * media files, including model selection, timing constraints, and audio properties.
 *
 * @property sourceLanguage ISO 639-1 code of the source audio. Use "auto" for auto-detection.
 * @property targetLanguages List of ISO 639-1 codes to translate the subtitles into.
 * @property model The [WhisperModel] size to use for transcription. Larger models are more accurate but slower.
 * @property outputFormat The desired subtitle format (e.g., SRT, ASS, VTT).
 * @property generateTranslatedAudio Whether to generate AI-voiced audio for the target languages.
 * @property keepOriginalAudio Whether to preserve the original audio track in the final output if muxing.
 * @property enableSilenceDetection If true, uses VAD (Voice Activity Detection) to skip silent parts.
 * @property maxSubtitleLineLength Maximum number of characters allowed per subtitle line.
 * @property maxSubtitleDurationMs Maximum duration for a single subtitle segment in milliseconds.
 * @property minSubtitleDurationMs Minimum duration for a single subtitle segment in milliseconds.
 * @property progressIntervalMs How often to report progress updates to listeners.
 * @property chunkDurationMs Duration of audio chunks processed at once by the recognition engine.
 * @property sampleRateHz Input sample rate. Fixed at 16,000Hz for Whisper compatibility.
 * @property channels Number of audio channels. Fixed at 1 (Mono) for Whisper compatibility.
 * @property bitsPerSample Bit depth of audio. Fixed at 16-bit for Whisper compatibility.
 */
@Keep
data class SubtitleConfig @JvmOverloads constructor(
    val sourceLanguage: String = "auto",
    val targetLanguages: List<String> = emptyList(),
    val model: WhisperModel = WhisperModel.BASE,
    val outputFormat: SubtitleFormat = SubtitleFormat.SRT,
    val generateTranslatedAudio: Boolean = false,
    val keepOriginalAudio: Boolean = true,
    val enableSilenceDetection: Boolean = true,
    val maxSubtitleLineLength: Int = 42,
    val maxSubtitleDurationMs: Long = 7_000L,
    val minSubtitleDurationMs: Long = 900L,
    val progressIntervalMs: Long = 1_000L,
    val chunkDurationMs: Long = 30_000L,
    val sampleRateHz: Int = 16_000,
    val channels: Int = 1,
    val bitsPerSample: Int = 16,
) {
    init {
        require(sampleRateHz == 16_000) { "Speech input must be 16 kHz PCM." }
        require(channels == 1) { "Speech input must be mono." }
        require(bitsPerSample == 16) { "Speech input must be 16-bit PCM." }
        require(maxSubtitleLineLength >= 16) { "maxSubtitleLineLength must be at least 16." }
        require(minSubtitleDurationMs > 0) { "minSubtitleDurationMs must be positive." }
        require(maxSubtitleDurationMs >= minSubtitleDurationMs) {
            "maxSubtitleDurationMs must be greater than minSubtitleDurationMs."
        }
        require(chunkDurationMs in 5_000L..120_000L) { "chunkDurationMs must be between 5s and 120s." }
    }

    /**
     * Returns the [sourceLanguage] normalized to lowercase US locale.
     */
    fun normalizedSourceLanguage(): String = sourceLanguage.lowercase(Locale.US)
}
