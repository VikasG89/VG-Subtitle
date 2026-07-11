package com.vg.subtitle.subtitle

import com.vg.subtitle.api.model.Segment


class SubtitleMerger {
    fun mergeCloseSegments(segments: List<Segment>, maxGapMs: Long = 250L): List<Segment> {
        if (segments.isEmpty()) return emptyList()
        val merged = mutableListOf<Segment>()
        var current = segments.first()
        for (next in segments.drop(1)) {
            if (next.startMs - current.endMs <= maxGapMs && sameLanguage(current, next)) {
                current = current.copy(endMs = next.endMs, text = "${current.text} ${next.text}".trim())
            } else {
                merged += current
                current = next
            }
        }
        merged += current
        return merged.mapIndexed { index, segment -> segment.copy(index = index + 1) }
    }

    private fun sameLanguage(first: Segment, second: Segment): Boolean = first.language == second.language
}
