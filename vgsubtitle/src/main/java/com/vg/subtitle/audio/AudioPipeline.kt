package com.vg.subtitle.audio

import com.vg.subtitle.api.AudioGenerationException
import com.vg.subtitle.recognizer.Segment
import java.io.File

interface TextToSpeechEngine {
    suspend fun synthesize(segment: Segment, language: String, outputFile: File): File
}

class AudioGenerator(private val engine: TextToSpeechEngine) {
    suspend fun generate(segments: List<Segment>, language: String, outputDir: File): List<File> {
        outputDir.mkdirs()
        return segments.map { segment ->
            val output = File(outputDir, "${language}_${segment.index}.wav")
            runCatching { engine.synthesize(segment, language, output) }
                .getOrElse { throw AudioGenerationException("Failed to synthesize segment ${segment.index}.", it) }
        }
    }
}

class AudioSynchronizer {
    data class TimedAudio(val segment: Segment, val audio: File)

    fun align(segments: List<Segment>, audioFiles: List<File>): List<TimedAudio> {
        require(segments.size == audioFiles.size) { "Segment and audio counts must match." }
        return segments.zip(audioFiles).map { (segment, file) -> TimedAudio(segment, file) }
    }
}

interface AudioMixer {
    suspend fun mix(primaryAudio: File, translatedAudio: List<AudioSynchronizer.TimedAudio>, outputFile: File): File
}

class PassthroughAudioMixer : AudioMixer {
    override suspend fun mix(
        primaryAudio: File,
        translatedAudio: List<AudioSynchronizer.TimedAudio>,
        outputFile: File,
    ): File {
        primaryAudio.copyTo(outputFile, overwrite = true)
        return outputFile
    }
}
