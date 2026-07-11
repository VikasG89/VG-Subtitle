package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * Main JNI bridge for communication with C++ runtime.
 * Implementation follows the "Native Interface Contract".
 */
@Keep
object NativeBridge {

    init {
        NativeLibraryLoader.load()
    }

    // Native Interface Contract
    external fun initialize(modelPath: String): Long
    external fun verifyModels(modelPath: String): Boolean
    external fun generateSubtitles(
        contextPtr: Long,
        pcmPath: String,
        chunkDurationMs: Long,
        language: String
    ): List<Segment>
    
    external fun pause(contextPtr: Long)
    external fun resume(contextPtr: Long)
    external fun cancel(contextPtr: Long)
    external fun release(contextPtr: Long)
    
    external fun getStatus(contextPtr: Long): Int
    external fun getStatistics(contextPtr: Long): Map<String, Float>

    // Core ASR functionality (internal use)
    external fun detectLanguage(contextPtr: Long, pcmPath: String): String
}
