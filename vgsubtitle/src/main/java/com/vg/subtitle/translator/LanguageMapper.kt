package com.vg.subtitle.translator

import java.util.Locale

object LanguageMapper {
    private val namesToCodes = mapOf(
        "english" to "en",
        "hindi" to "hi",
        "marathi" to "mr",
        "gujarati" to "gu",
        "tamil" to "ta",
        "telugu" to "te",
        "kannada" to "kn",
        "malayalam" to "ml",
        "punjabi" to "pa",
        "bengali" to "bn",
        "odia" to "or",
        "assamese" to "as",
        "urdu" to "ur",
        "sanskrit" to "sa",
        "konkani" to "kok",
        "nepali" to "ne",
        "french" to "fr",
        "german" to "de",
        "spanish" to "es",
        "japanese" to "ja",
        "chinese" to "zh",
        "arabic" to "ar",
        "russian" to "ru",
        "korean" to "ko",
        "italian" to "it",
        "portuguese" to "pt",
        "turkish" to "tr",
        "vietnamese" to "vi",
        "thai" to "th",
        "indonesian" to "id",
    )

    @JvmStatic
    fun normalize(language: String): String {
        val value = language.trim().lowercase(Locale.US).replace("_", "-")
        return namesToCodes[value] ?: value.takeIf { it.length in 2..8 } ?: language
    }

    @JvmStatic
    fun supportedLanguages(): Set<String> = namesToCodes.values.toSortedSet()
}
