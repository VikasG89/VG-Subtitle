package com.vg.subtitle.api.exception

import androidx.annotation.Keep

@Keep
open class VGSubtitleException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

@Keep class UnsupportedCodecException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class CorruptedVideoException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class MissingPermissionException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class StorageUnavailableException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class LowMemorySubtitleException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class RecognitionException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class TranslationException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
@Keep class AudioGenerationException(message: String, cause: Throwable? = null) : VGSubtitleException(message, cause)
