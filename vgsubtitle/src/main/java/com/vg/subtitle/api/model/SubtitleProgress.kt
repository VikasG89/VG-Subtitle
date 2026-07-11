package com.vg.subtitle.api.model

import androidx.annotation.Keep

/**
 * Data class representing the current progress of a subtitle, translation, or audio task.
 *
 * @property percent Completion percentage (0-100).
 * @property processedMs Total amount of audio processed so far in milliseconds.
 * @property totalMs Total duration of the audio being processed in milliseconds.
 * @property currentTimestampMs The timestamp currently being processed in the input file.
 * @property etaMs Estimated time remaining for the task to complete in milliseconds.
 * @property message A human-readable status message describing the current step.
 */
@Keep
data class SubtitleProgress @JvmOverloads constructor(
    val percent: Int,
    val processedMs: Long,
    val totalMs: Long,
    val currentTimestampMs: Long,
    val etaMs: Long,
    val message: String = "",
) {
    init {
        require(percent in 0..100) { "percent must be between 0 and 100." }
        require(processedMs >= 0) { "processedMs must be non-negative." }
        require(totalMs >= 0) { "totalMs must be non-negative." }
        require(currentTimestampMs >= 0) { "currentTimestampMs must be non-negative." }
        require(etaMs >= 0) { "etaMs must be non-negative." }
    }
}
