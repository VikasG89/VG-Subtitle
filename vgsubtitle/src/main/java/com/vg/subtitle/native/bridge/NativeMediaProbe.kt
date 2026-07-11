package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * JNI bridge for probing media file information.
 */
@Keep
object NativeMediaProbe {

    init {
        NativeLibraryLoader.load()
    }

    external fun getDurationMs(path: String): Long
    external fun hasAudio(path: String): Boolean
    external fun getFormat(path: String): String
}
