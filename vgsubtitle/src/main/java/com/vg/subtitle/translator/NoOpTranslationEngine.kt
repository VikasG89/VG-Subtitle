package com.vg.subtitle.translator

/** Explicit fallback for tests and integration bring-up. It preserves text and changes only language metadata. */
class NoOpTranslationEngine : TranslationEngine {
    override suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String): String = text
}
