package com.vg.subtitle.subtitle

import com.vg.subtitle.api.model.Segment
import java.io.File

class SrtParser {
    fun parse(file: File): List<Segment> {
        val blocks = file.readText(Charsets.UTF_8)
            .replace("\r\n", "\n")
            .split(Regex("\n\\s*\n"))
            .filter { it.isNotBlank() }
        return blocks.mapIndexedNotNull { fallbackIndex, block ->
            val lines = block.lines().filter { it.isNotBlank() }
            if (lines.size < 2) return@mapIndexedNotNull null
            val timeLineIndex = lines.indexOfFirst { it.contains("-->") }
            if (timeLineIndex < 0) return@mapIndexedNotNull null
            val index = lines.firstOrNull()?.toIntOrNull() ?: fallbackIndex + 1
            val times = lines[timeLineIndex].split("-->")
            val text = lines.drop(timeLineIndex + 1).joinToString("\n")
            Segment(
                index = index,
                startMs = parseTimestamp(times[0].trim()),
                endMs = parseTimestamp(times[1].trim()),
                text = text,
            )
        }
    }

    private fun parseTimestamp(value: String): Long {
        val parts = value.replace(',', '.').split(':', '.')
        require(parts.size == 4) { "Invalid SRT timestamp: $value" }
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()
        val millis = parts[3].padEnd(3, '0').take(3).toLong()
        return hours * 3_600_000L + minutes * 60_000L + seconds * 1_000L + millis
    }
}
