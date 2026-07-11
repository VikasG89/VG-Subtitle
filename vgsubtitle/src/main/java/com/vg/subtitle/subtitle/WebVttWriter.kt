package com.vg.subtitle.subtitle

import com.vg.subtitle.api.model.Segment


class WebVttWriter {
    fun format(segments: List<Segment>): String = buildString {
        append("WEBVTT\n\n")
        segments.forEach { segment ->
            append(TimestampGenerator.toWebVtt(segment.startMs))
            append(" --> ")
            append(TimestampGenerator.toWebVtt(segment.endMs))
            append('\n')
            append(segment.text.trim())
            append("\n\n")
        }
    }
}
