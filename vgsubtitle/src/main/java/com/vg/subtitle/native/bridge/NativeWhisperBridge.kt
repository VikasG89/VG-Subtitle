package com.vg.subtitle.native.bridge

import com.vg.subtitle.api.model.Segment

/**
 * Interface for the native Whisper engine bridge.
 * 
 * Provides methods for model management, language detection, and transcription.
 */
interface NativeWhisperBridge {
    /**
     * Loads a Whisper model into memory.
     * 
     * @param modelPath Absolute path to the .bin model file.
     */
    suspend fun loadModel(modelPath: String)

    /** Returns true if a model is currently loaded and ready. */
    fun isModelLoaded(): Boolean

    /**
     * Detects the language of the audio file.
     * 
     * @param pcmPath Absolute path to the WAV file (16kHz mono).
     * @return ISO 639-1 language code or "und" if detection fails.
     */
    suspend fun detectLanguage(pcmPath: String): String

    /**
     * Transcribes an audio file.
     * 
     * @param pcmPath Absolute path to the WAV file.
     * @param chunkDurationMs Duration of chunks to process.
     * @param language Targeted language code or "auto".
     * @return List of transcribed [Segment]s.
     */
    suspend fun transcribe(pcmPath: String, chunkDurationMs: Long, language: String = "auto"): List<Segment>
}
