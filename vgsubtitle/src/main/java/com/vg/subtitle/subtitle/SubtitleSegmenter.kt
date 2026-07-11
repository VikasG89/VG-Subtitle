package com.vg.subtitle.subtitle

import com.vg.subtitle.recognizer.Segment

class SubtitleSegmenter(
    private val maxLineLength: Int,
    private val minDurationMs: Long,
    private val maxDurationMs: Long,
) {
    fun normalize(segments: List<Segment>): List<Segment> {
        val split = segments.flatMap { splitLongText(it) }
        return preventOverlap(split).mapIndexed { index, segment -> segment.copy(index = index + 1) }
    }

    private fun splitLongText(segment: Segment): List<Segment> {
        val words = segment.text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        if (words.isEmpty()) return emptyList()

        val lines = mutableListOf<String>()
        var current = StringBuilder()
        for (word in words) {
            val candidateLength = current.length + if (current.isEmpty()) word.length else word.length + 1
            if (candidateLength > maxLineLength && current.isNotEmpty()) {
                lines += current.toString()
                current = StringBuilder(word)
            } else {
                if (current.isNotEmpty()) current.append(' ')
                current.append(word)
            }
        }
        if (current.isNotEmpty()) lines += current.toString()

        if (lines.size == 1) return listOf(segment.withDurationBounds())
        val totalDuration = (segment.endMs - segment.startMs).coerceAtLeast(minDurationMs * lines.size)
        val partDuration = (totalDuration / lines.size).coerceIn(minDurationMs, maxDurationMs)
        return lines.mapIndexed { index, text ->
            val start = segment.startMs + partDuration * index
            val end = if (index == lines.lastIndex) segment.endMs.coerceAtLeast(start + minDurationMs) else start + partDuration
            segment.copy(startMs = start, endMs = end, text = text)
        }
    }

    private fun Segment.withDurationBounds(): Segment {
        val duration = (endMs - startMs).coerceIn(minDurationMs, maxDurationMs)
        return copy(endMs = startMs + duration)
    }

    private fun preventOverlap(segments: List<Segment>): List<Segment> {
        var previousEnd = 0L
        return segments.map { segment ->
            val start = segment.startMs.coerceAtLeast(previousEnd)
            val end = segment.endMs.coerceAtLeast(start + minDurationMs)
            previousEnd = end
            segment.copy(startMs = start, endMs = end)
        }
    }
}
