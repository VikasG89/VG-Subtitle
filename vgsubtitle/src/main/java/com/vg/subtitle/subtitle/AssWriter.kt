package com.vg.subtitle.subtitle

import com.vg.subtitle.recognizer.Segment

class AssWriter {
    fun format(segments: List<Segment>): String = buildString {
        append("[Script Info]\nScriptType: v4.00+\nCollisions: Normal\n\n")
        append("[V4+ Styles]\n")
        append("Format: Name, Fontname, Fontsize, PrimaryColour, BackColour, Bold, Italic, ")
        append("Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, ")
        append("Shadow, Alignment, MarginL, MarginR, MarginV, Encoding\n")
        append("Style: Default,Arial,42,&H00FFFFFF,&H00000000,0,0,0,0,100,100,0,0,1,2,0,2,20,20,36,1\n\n")
        append("[Events]\n")
        append("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text\n")
        segments.forEach { segment ->
            append("Dialogue: 0,")
            append(TimestampGenerator.toAss(segment.startMs)).append(',')
            append(TimestampGenerator.toAss(segment.endMs)).append(',')
            append("Default,,0,0,0,,")
            append(segment.text.replace("\n", "\\N"))
            append('\n')
        }
    }
}
