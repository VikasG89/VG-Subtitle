package com.vg.subtitle.recognizer

import android.content.Context
import androidx.annotation.Keep
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

/** Java-friendly bridge for apps that expose Whisper/ASR calls as blocking methods. */
@Keep
interface BlockingNativeWhisperBridge {
    fun loadModel(modelPath: String)
    fun isModelLoaded(): Boolean
    fun detectLanguage(pcmPath: String): String
    fun transcribe(pcmPath: String, chunkDurationMs: Long, language: String): List<Segment>
}

/** Adapts a blocking Java bridge to the suspend-based native bridge used internally. */
@Keep
class BlockingNativeWhisperBridgeAdapter @JvmOverloads constructor(
    private val delegate: BlockingNativeWhisperBridge,
    executor: Executor = Executors.newSingleThreadExecutor(),
) : NativeWhisperBridge {
    private val dispatcher = executor.asCoroutineDispatcher()

    override suspend fun loadModel(modelPath: String) = withContext(dispatcher) {
        delegate.loadModel(modelPath)
    }

    override fun isModelLoaded(): Boolean = delegate.isModelLoaded()

    override suspend fun detectLanguage(pcmPath: String): String = withContext(dispatcher) {
        delegate.detectLanguage(pcmPath)
    }

    override suspend fun transcribe(pcmPath: String, chunkDurationMs: Long, language: String): List<Segment> = withContext(dispatcher) {
        delegate.transcribe(pcmPath, chunkDurationMs, language)
    }
}

/** Factory methods that keep Java integration code concise. */
@Keep
object WhisperEngines {
    @JvmStatic
    fun fromBlockingBridge(context: Context, bridge: BlockingNativeWhisperBridge): WhisperEngine {
        return WhisperEngine(ModelManager(context.applicationContext), BlockingNativeWhisperBridgeAdapter(bridge))
    }
}
