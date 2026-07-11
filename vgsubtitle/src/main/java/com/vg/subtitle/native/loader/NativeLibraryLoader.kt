package com.vg.subtitle.native.loader

import android.util.Log
import androidx.annotation.Keep

/** Manages loading of the native shared libraries. */
@Keep
object NativeLibraryLoader {
    private const val TAG = "VGSubtitleLoader"
    private const val LIB_NAME = "vgsubtitle"

    @Volatile
    private var isLoaded = false

    fun load() {
        if (isLoaded) return
        synchronized(this) {
            if (isLoaded) return
            try {
                Log.i(TAG, "Loading native library: $LIB_NAME")
                System.loadLibrary(LIB_NAME)
                isLoaded = true
                Log.i(TAG, "Native library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Failed to load native library: $LIB_NAME", e)
                throw RuntimeException("Failed to load native library: $LIB_NAME", e)
            }
        }
    }

    fun isLoaded(): Boolean = isLoaded
}
