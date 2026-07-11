package com.vg.subtitle.translator

import com.vg.subtitle.api.exception.TranslationException
import com.vg.subtitle.api.model.Segment

class SubtitleTranslator(private val engine: TranslationEngine) {
    suspend fun translate(
        segments: List<Segment>,
        sourceLanguage: String,
        targetLanguages: List<String>,
    ): Map<String, List<Segment>> {
        if (targetLanguages.isEmpty()) return emptyMap()
        return targetLanguages.associate { language ->
            val code = LanguageMapper.normalize(language)
            code to runCatching {
                engine.translateSegments(segments, sourceLanguage, code)
            }.getOrElse { throw TranslationException(
                "Failed to translate subtitles to $code.",
                it
            ) as Throwable }
        }
    }
}
