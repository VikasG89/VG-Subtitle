package com.vg.subtitle.engine.subtitle

import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.api.model.SubtitleFormat

class SubtitleFormatter {
    fun format(segments: List<Segment>, format: SubtitleFormat): String = when (format) {
        SubtitleFormat.SRT -> SrtWriter().format(segments)
        SubtitleFormat.WEBVTT -> WebVttWriter().format(segments)
        SubtitleFormat.ASS,
        SubtitleFormat.SSA -> AssWriter().format(segments)
        SubtitleFormat.TEXT -> segments.joinToString(separator = "\n") { it.text }
        SubtitleFormat.JSON -> JsonTranscriptWriter().format(segments)
    }

    fun normalizeAndFormat(segments: List<Segment>, config: SubtitleConfig): String {
        val normalized = SubtitleSegmenter(
            maxLineLength = config.maxSubtitleLineLength,
            minDurationMs = config.minSubtitleDurationMs,
            maxDurationMs = config.maxSubtitleDurationMs,
        ).normalize(segments)
        return format(normalized, config.outputFormat)
    }
}
