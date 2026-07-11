package com.vg.subtitle.recognizer

import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.exception.RecognitionException
import com.vg.subtitle.api.model.WhisperModel
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.model.manager.ModelManager
import com.vg.subtitle.native.bridge.NativeWhisper
import com.vg.subtitle.native.bridge.NativeWhisperBridge
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** 
 * Offline Whisper adapter. 
 * 
 * This engine acts as a high-level wrapper around a [NativeWhisperBridge] to provide 
 * speech-to-text capabilities using OpenAI's Whisper models.
 * 
 * @property modelManager Component responsible for locating and verifying model files.
 * @property bridge The low-level interface to the native Whisper implementation (e.g., whisper.cpp).
 */
class WhisperEngine @JvmOverloads constructor(
    private val modelManager: ModelManager,
    private val bridge: NativeWhisperBridge = NativeWhisper(),
) : SpeechRecognizerEngine {
    private var currentModel: WhisperModel = WhisperModel.BASE

    /**
     * Loads the specified Whisper model into the native engine.
     * 
     * @param model The model size to load.
     * @throws RecognitionException if the model cannot be loaded.
     */
    override suspend fun setModel(model: WhisperModel) {
        modelManager.requireModel(model)
        currentModel = model
        bridge.loadModel(modelManager.getModelFile(model).absolutePath)
    }

    /**
     * Detects the language of the provided audio file.
     * 
     * @param audioPcm16kMono A 16kHz mono WAV file.
     * @return ISO 639-1 language code.
     */
    override suspend fun detectLanguage(audioPcm16kMono: File): String {
        ensureModelLoaded()
        return bridge.detectLanguage(audioPcm16kMono.absolutePath)
    }

    /**
     * Transcribes audio and emits segments as they are produced.
     * 
     * @param audioPcm16kMono The audio file to transcribe.
     * @param config Transcription parameters.
     * @return A [Flow] of transcribed segments.
     */
    override fun transcribeStreaming(audioPcm16kMono: File, config: SubtitleConfig): Flow<Segment> = flow {
        ensureModelLoaded()
        bridge.transcribe(
            audioPcm16kMono.absolutePath,
            config.chunkDurationMs,
            config.normalizedSourceLanguage()
        ).forEach { emit(it) }
    }

    /**
     * Transcribes the entire audio file and returns all segments.
     * 
     * @param audioPcm16kMono The audio file to transcribe.
     * @param config Transcription parameters.
     * @return List of transcribed segments.
     */
    override suspend fun transcribeBatch(audioPcm16kMono: File, config: SubtitleConfig): List<Segment> {
        ensureModelLoaded()
        return bridge.transcribe(
            audioPcm16kMono.absolutePath,
            config.chunkDurationMs,
            config.normalizedSourceLanguage()
        )
    }

    /**
     * Resumes transcription from a specific timestamp.
     */
    override suspend fun resumeTranscription(audioPcm16kMono: File, config: SubtitleConfig, fromMs: Long): List<Segment> {
        return transcribeBatch(audioPcm16kMono, config).filter { it.endMs >= fromMs }
    }

    private suspend fun ensureModelLoaded() {
        if (!bridge.isModelLoaded()) setModel(currentModel)
    }
}

