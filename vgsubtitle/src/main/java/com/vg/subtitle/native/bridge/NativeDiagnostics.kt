package com.vg.subtitle.native.bridge

import androidx.annotation.Keep

/**
 * Provides diagnostic information about the native runtime.
 */
@Keep
object NativeDiagnostics {
    fun getSystemInfo(): String {
        return "Android ABI: ${android.os.Build.SUPPORTED_ABIS.joinToString()}"
    }
}
