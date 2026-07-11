package com.vg.subtitle.engine.transcription

import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.api.model.WhisperModel
import com.vg.subtitle.model.manager.ModelManager
import com.vg.subtitle.native.bridge.NativeWhisper
import com.vg.subtitle.native.bridge.NativeWhisperBridge
import com.vg.subtitle.recognizer.SpeechRecognizerEngine
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Offline Whisper adapter. Inject a NativeWhisperBridge backed by whisper.cpp or another local runtime. */
class WhisperEngine @JvmOverloads constructor(
    private val modelManager: ModelManager,
    private val bridge: NativeWhisperBridge = NativeWhisper(),
) : SpeechRecognizerEngine {
    private var currentModel: WhisperModel = WhisperModel.BASE

    override suspend fun setModel(model: WhisperModel) {
        modelManager.requireModel(model)
        currentModel = model
        bridge.loadModel(modelManager.getModelFile(model).absolutePath)
    }

    override suspend fun detectLanguage(audioPcm16kMono: File): String {
        ensureModelLoaded()
        return bridge.detectLanguage(audioPcm16kMono.absolutePath)
    }

    override fun transcribeStreaming(audioPcm16kMono: File, config: SubtitleConfig): Flow<Segment> = flow {
        ensureModelLoaded()
        bridge.transcribe(
            audioPcm16kMono.absolutePath,
            config.chunkDurationMs,
            config.normalizedSourceLanguage()
        ).forEach { emit(it) }
    }

    override suspend fun transcribeBatch(audioPcm16kMono: File, config: SubtitleConfig): List<Segment> {
        ensureModelLoaded()
        return bridge.transcribe(
            audioPcm16kMono.absolutePath,
            config.chunkDurationMs,
            config.normalizedSourceLanguage()
        )
    }

    override suspend fun resumeTranscription(audioPcm16kMono: File, config: SubtitleConfig, fromMs: Long): List<Segment> {
        return transcribeBatch(audioPcm16kMono, config).filter { it.endMs >= fromMs }
    }

    private suspend fun ensureModelLoaded() {
        if (!bridge.isModelLoaded()) setModel(currentModel)
    }
}
