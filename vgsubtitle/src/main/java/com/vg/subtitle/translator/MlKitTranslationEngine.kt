package com.vg.subtitle.translator

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.vg.subtitle.recognizer.Segment
import kotlinx.coroutines.tasks.await

/**
 * ML Kit implementation of [TranslationEngine] for offline translation.
 *
 * This engine uses Google's ML Kit Translate API to perform text translation locally on the device.
 * It automatically handles downloading the required language models if they are not already present.
 */
class MlKitTranslationEngine : TranslationEngine {

    /**
     * Translates a single string of text.
     * 
     * @param text The text to translate.
     * @param sourceLanguage ISO 639-1 code of the source language.
     * @param targetLanguage ISO 639-1 code of the target language.
     * @return The translated text.
     */
    override suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(LanguageMapper.normalize(sourceLanguage))
            .setTargetLanguage(LanguageMapper.normalize(targetLanguage))
            .build()
        val translator = Translation.getClient(options)
        return try {
            translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            translator.translate(text).await()
        } finally {
            translator.close()
        }
    }

    /**
     * Translates a list of [Segment] objects.
     * 
     * @param segments The segments to translate.
     * @param sourceLanguage ISO 639-1 code of the source language.
     * @param targetLanguage ISO 639-1 code of the target language.
     * @return A new list of segments with translated text and updated language code.
     */
    override suspend fun translateSegments(
        segments: List<Segment>,
        sourceLanguage: String,
        targetLanguage: String
    ): List<Segment> {
        if (segments.isEmpty()) return emptyList()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(LanguageMapper.normalize(sourceLanguage))
            .setTargetLanguage(LanguageMapper.normalize(targetLanguage))
            .build()
        val translator = Translation.getClient(options)

        return try {
            translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            segments.map {
                it.copy(text = translator.translate(it.text).await(), language = targetLanguage)
            }
        } finally {
            translator.close()
        }
    }
}
