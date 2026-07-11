package com.vg.subtitle.utils

object AudioUtils {
    const val SPEECH_SAMPLE_RATE_HZ = 16_000
    const val SPEECH_CHANNELS = 1
    const val SPEECH_BITS_PER_SAMPLE = 16

    @JvmStatic
    fun bytesForPcm16(durationMs: Long, sampleRateHz: Int = SPEECH_SAMPLE_RATE_HZ, channels: Int = SPEECH_CHANNELS): Long {
        return (durationMs.coerceAtLeast(0) * sampleRateHz * channels * 2L) / 1_000L
    }
}
