package com.vg.subtitle.translator

import com.vg.subtitle.recognizer.Segment

/** Offline translation engine. Implementations can wrap ML Kit, ONNX, or custom local models. */
interface TranslationEngine {
    suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String): String
    suspend fun translateSegments(
        segments: List<Segment>,
        sourceLanguage: String,
        targetLanguage: String,
    ): List<Segment> = segments.map {
        it.copy(text = translate(it.text, sourceLanguage, targetLanguage), language = targetLanguage)
    }
}
