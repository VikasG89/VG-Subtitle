package com.vg.subtitle.subtitle

import com.vg.subtitle.recognizer.Segment

class SrtWriter {
    fun format(segments: List<Segment>): String = buildString {
        segments.forEachIndexed { index, segment ->
            append(index + 1).append('\n')
            append(TimestampGenerator.toSrt(segment.startMs))
            append(" --> ")
            append(TimestampGenerator.toSrt(segment.endMs))
            append('\n')
            append(segment.text.trim())
            append("\n\n")
        }
    }
}
