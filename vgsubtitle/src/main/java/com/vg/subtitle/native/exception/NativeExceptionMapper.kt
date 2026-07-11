package com.vg.subtitle.native.exception

import androidx.annotation.Keep
import com.vg.subtitle.api.exception.*
import android.util.Log

/**
 * Maps native error codes to typed SDK exceptions.
 */
@Keep
object NativeExceptionMapper {
    private const val TAG = "NativeExceptionMapper"

    @JvmStatic
    fun throwFromNative(errorCode: Int, message: String) {
        val exception = when (errorCode) {
            1 -> RecognitionException(message)
            2 -> UnsupportedCodecException(message)
            3 -> StorageUnavailableException(message)
            4 -> LowMemorySubtitleException(message)
            else -> VGSubtitleException("Native error $errorCode: $message")
        }
        Log.e(TAG, "Native exception: $message (code: $errorCode)")
        throw exception
    }
}
