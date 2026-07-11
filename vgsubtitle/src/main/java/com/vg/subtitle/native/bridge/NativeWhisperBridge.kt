package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * Native bridge for Whisper.cpp inference.
 */
@Keep
object NativeWhisperBridge {

    init {
        NativeLibraryLoader.load()
    }

    external fun initializeWhisper(modelPath: String): Long
    external fun releaseWhisper(runtimePtr: Long)
    
    external fun transcribeWhisper(
        runtimePtr: Long,
        pcmPath: String,
        language: String
    ): List<Segment>

    // Legacy support (mapping to new names if needed)
    suspend fun loadModel(modelPath: String) { /* handled via initialize */ }
}
