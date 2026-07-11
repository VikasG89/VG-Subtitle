package com.vg.subtitle.api.model

import androidx.annotation.Keep

/** Data class representing the result of a completed subtitle task. */
@Keep
data class SubtitleResult(
    val outputPath: String,
    val language: String,
    val durationMs: Long,
    val segmentCount: Int,
    val success: Boolean = true,
    val errorMessage: String? = null
)
