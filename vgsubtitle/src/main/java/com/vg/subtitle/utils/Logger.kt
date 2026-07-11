package com.vg.subtitle.utils

import android.util.Log
import com.vg.subtitle.BuildConfig

object Logger {
    @JvmStatic var enabled: Boolean = BuildConfig.DEBUG
    @JvmStatic var tag: String = "VGSubtitle"

    @JvmStatic fun debug(message: String) {
        if (enabled) Log.d(tag, message)
    }

    @JvmStatic fun info(message: String) {
        if (enabled) Log.i(tag, message)
    }

    @JvmStatic fun warning(message: String, throwable: Throwable? = null) {
        if (enabled) Log.w(tag, message, throwable)
    }

    @JvmStatic fun error(message: String, throwable: Throwable? = null) {
        if (enabled) Log.e(tag, message, throwable)
    }
}
