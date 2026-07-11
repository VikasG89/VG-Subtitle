package com.vg.subtitle.utils

/**
 * Utility functions for time-related calculations and progress estimation.
 */
object TimeUtils {
    /**
     * Estimates the remaining time (ETA) for a task.
     * 
     * @param processedMs Amount of work completed in milliseconds.
     * @param totalMs Total amount of work to be done in milliseconds.
     * @param elapsedMs Time taken to complete the [processedMs] work.
     * @return Estimated remaining time in milliseconds.
     */
    @JvmStatic
    fun estimateEta(processedMs: Long, totalMs: Long, elapsedMs: Long): Long {
        if (processedMs <= 0L || totalMs <= 0L || processedMs >= totalMs) return 0L
        val rate = elapsedMs.toDouble() / processedMs.toDouble()
        return ((totalMs - processedMs) * rate).toLong().coerceAtLeast(0L)
    }

    /**
     * Calculates the completion percentage.
     * 
     * @param processedMs Amount of work completed.
     * @param totalMs Total amount of work.
     * @return Percentage integer between 0 and 100.
     */
    @JvmStatic
    fun percent(processedMs: Long, totalMs: Long): Int {
        if (totalMs <= 0L) return 0
        return ((processedMs * 100L) / totalMs).toInt().coerceIn(0, 100)
    }
}
