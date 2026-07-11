package com.vg.subtitle.subtitle

import java.util.Locale

object TimestampGenerator {
    fun toSrt(ms: Long): String {
        val safe = ms.coerceAtLeast(0)
        val hours = safe / 3_600_000
        val minutes = (safe % 3_600_000) / 60_000
        val seconds = (safe % 60_000) / 1_000
        val millis = safe % 1_000
        return String.format(Locale.US, "%02d:%02d:%02d,%03d", hours, minutes, seconds, millis)
    }

    fun toWebVtt(ms: Long): String = toSrt(ms).replace(',', '.')

    fun toAss(ms: Long): String {
        val safe = ms.coerceAtLeast(0)
        val hours = safe / 3_600_000
        val minutes = (safe % 3_600_000) / 60_000
        val seconds = (safe % 60_000) / 1_000
        val centis = (safe % 1_000) / 10
        return String.format(Locale.US, "%d:%02d:%02d.%02d", hours, minutes, seconds, centis)
    }
}
