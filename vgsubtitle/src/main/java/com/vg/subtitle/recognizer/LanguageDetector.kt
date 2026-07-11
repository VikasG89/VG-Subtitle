package com.vg.subtitle.recognizer

/** Lightweight language detector facade for recognizer output. */
interface LanguageDetector {
    suspend fun detect(text: String, fallback: String = "und"): String
}

class ScriptLanguageDetector : LanguageDetector {
    override suspend fun detect(text: String, fallback: String): String {
        val sample = text.firstOrNull { !it.isWhitespace() } ?: return fallback
        val block = Character.UnicodeBlock.of(sample)
        return when (block) {
            Character.UnicodeBlock.DEVANAGARI -> "hi"
            Character.UnicodeBlock.ARABIC -> "ar"
            Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,
            Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS -> "zh"
            Character.UnicodeBlock.HIRAGANA,
            Character.UnicodeBlock.KATAKANA -> "ja"
            Character.UnicodeBlock.CYRILLIC -> "ru"
            else -> fallback
        }
    }
}
