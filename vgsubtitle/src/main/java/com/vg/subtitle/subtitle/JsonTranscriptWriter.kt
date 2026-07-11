package com.vg.subtitle.subtitle

import com.vg.subtitle.recognizer.Segment

class JsonTranscriptWriter {
    fun format(segments: List<Segment>): String = buildString {
        append("[\n")
        segments.forEachIndexed { index, segment ->
            append("  {")
            append("\"index\":").append(segment.index).append(',')
            append("\"startMs\":").append(segment.startMs).append(',')
            append("\"endMs\":").append(segment.endMs).append(',')
            append("\"language\":\"").append(segment.language.escapeJson()).append("\",")
            append("\"confidence\":").append(segment.confidence).append(',')
            append("\"text\":\"").append(segment.text.escapeJson()).append("\"")
            append("}")
            if (index != segments.lastIndex) append(',')
            append('\n')
        }
        append("]\n")
    }

    private fun String.escapeJson(): String = buildString {
        this@escapeJson.forEach { char ->
            when (char) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(char)
            }
        }
    }
}
