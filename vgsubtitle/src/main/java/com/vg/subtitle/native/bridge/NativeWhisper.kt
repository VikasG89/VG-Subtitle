package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.native.session.NativeSession

/**
 * Default implementation of [NativeWhisperBridge] that delegates to [NativeBridge]
 * and manages a [NativeSession].
 * 
 * This class handles the lifecycle of the Whisper model in memory and provides
 * methods for language detection and transcription.
 */
@Keep
class NativeWhisper : NativeWhisperBridge {

    private var session: NativeSession? = null

    /**
     * Loads a Whisper model into memory.
     * 
     * @param modelPath Absolute path to the .bin model file.
     */
    override suspend fun loadModel(modelPath: String) {
        session?.close()
        session = NativeSession(modelPath).apply {
            initialize()
        }
    }

    /** Returns true if a model is currently loaded and ready. */
    override fun isModelLoaded(): Boolean = session?.isInitialized() == true

    /**
     * Detects the language of the audio file.
     * 
     * @param pcmPath Absolute path to the WAV file (16kHz mono).
     * @return ISO 639-1 language code or "und" if detection fails.
     */
    override suspend fun detectLanguage(pcmPath: String): String {
        val ptr = session?.getPtr() ?: return "und"
        return NativeBridge.detectLanguage(ptr, pcmPath)
    }

    /**
     * Transcribes an audio file.
     * 
     * @param pcmPath Absolute path to the WAV file.
     * @param chunkDurationMs Duration of chunks to process.
     * @param language Targeted language code or "auto".
     * @return List of transcribed [Segment]s.
     */
    override suspend fun transcribe(pcmPath: String, chunkDurationMs: Long, language: String): List<Segment> {
        val ptr = session?.getPtr() ?: return emptyList()
        return NativeBridge.generateSubtitles(ptr, pcmPath, chunkDurationMs, language)
    }

    @Suppress("unused")
    protected fun finalize() {
        session?.close()
    }
}
