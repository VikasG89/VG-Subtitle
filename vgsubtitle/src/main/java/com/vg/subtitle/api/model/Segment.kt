package com.vg.subtitle.api.model

import androidx.annotation.Keep

/**
 * A timestamped speech or subtitle segment.
 *
 * Represents a single piece of transcribed text with its corresponding time range in the audio.
 *
 * @property index The sequential index of this segment.
 * @property startMs The start time of the segment in milliseconds.
 * @property endMs The end time of the segment in milliseconds.
 * @property text The transcribed or translated text content.
 * @property language ISO 639-1 language code of this segment. Defaults to "und" (undefined).
 * @property confidence The engine's confidence score for this transcription, from 0.0 to 1.0.
 */
@Keep
data class Segment @JvmOverloads constructor(
    val index: Int,
    val startMs: Long,
    val endMs: Long,
    val text: String,
    val language: String = "und",
    val confidence: Float = 0f,
) {
    init {
        require(index >= 0) { "index must be non-negative." }
        require(startMs >= 0) { "startMs must be non-negative." }
        require(endMs >= startMs) { "endMs must be greater than or equal to startMs." }
        require(confidence in 0f..1f) { "confidence must be between 0 and 1." }
    }
}
