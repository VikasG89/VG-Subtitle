package com.vg.subtitle.subtitle

import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.api.model.SubtitleFormat

/**
 * Responsible for formatting a list of [Segment] objects into various subtitle file formats.
 */
class SubtitleFormatter {
    /**
     * Formats the segments into the specified [SubtitleFormat].
     * 
     * @param segments The list of segments to format.
     * @param format The target format (e.g., SRT, VTT, JSON).
     * @return A string containing the formatted subtitle content.
     */
    fun format(segments: List<Segment>, format: SubtitleFormat): String = when (format) {
        SubtitleFormat.SRT -> SrtWriter().format(segments)
        SubtitleFormat.WEBVTT -> WebVttWriter().format(segments)
        SubtitleFormat.ASS,
        SubtitleFormat.SSA -> AssWriter().format(segments)
        SubtitleFormat.TEXT -> segments.joinToString(separator = "\n") { it.text }
        SubtitleFormat.JSON -> JsonTranscriptWriter().format(segments)
    }

    /**
     * Normalizes the segments based on configuration constraints and then formats them.
     * 
     * Normalization includes merging short segments and splitting long ones to fit 
     * character and duration limits specified in [SubtitleConfig].
     * 
     * @param segments The raw transcribed segments.
     * @param config The configuration containing limits and target format.
     * @return A string containing the normalized and formatted subtitle content.
     */
    fun normalizeAndFormat(segments: List<Segment>, config: SubtitleConfig): String {
        val normalized = SubtitleSegmenter(
            maxLineLength = config.maxSubtitleLineLength,
            minDurationMs = config.minSubtitleDurationMs,
            maxDurationMs = config.maxSubtitleDurationMs,
        ).normalize(segments)
        return format(normalized, config.outputFormat)
    }
}
