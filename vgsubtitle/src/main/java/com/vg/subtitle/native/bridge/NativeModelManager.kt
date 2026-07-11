package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * Manages native model verification and management.
 */
@Keep
object NativeModelManager {

    init {
        NativeLibraryLoader.load()
    }

    external fun verifyGguf(path: String): Boolean
}
