package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * JNI bridge for decoding audio to PCM float arrays.
 * 
 * Provides a Kotlin interface to the C++ AudioDecoder class.
 */
@Keep
object NativeAudioDecoder {

    init {
        NativeLibraryLoader.load()
    }

    /**
     * Decodes an audio file (typically WAV) to a normalized float array.
     * 
     * @param pcmPath Absolute path to the audio file.
     * @return FloatArray of samples in range [-1.0, 1.0], or null if decoding fails.
     */
    external fun decodeToFloatArray(pcmPath: String): FloatArray?
}
