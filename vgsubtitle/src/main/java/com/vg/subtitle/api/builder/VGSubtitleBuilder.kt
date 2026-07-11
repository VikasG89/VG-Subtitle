package com.vg.subtitle.api.builder

import android.content.Context
import com.vg.subtitle.api.VGSubtitleEngine
import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.audio.TextToSpeechEngine
import com.vg.subtitle.recognizer.SpeechRecognizerEngine
import com.vg.subtitle.translator.TranslationEngine

/** Builder for [VGSubtitleEngine]. */
class VGSubtitleBuilder(private val context: Context) {
    private var config = SubtitleConfig()
    private var speechEngine: SpeechRecognizerEngine? = null
    private var translationEngine: TranslationEngine? = null
    private var ttsEngine: TextToSpeechEngine? = null

    fun setConfig(config: SubtitleConfig) = apply { this.config = config }
    fun setSpeechEngine(engine: SpeechRecognizerEngine) = apply { this.speechEngine = engine }
    fun setTranslationEngine(engine: TranslationEngine) = apply { this.translationEngine = engine }
    fun setTtsEngine(engine: TextToSpeechEngine) = apply { this.ttsEngine = engine }

    fun build(): VGSubtitleEngine {
        val engine = VGSubtitleEngine(context, config)
        speechEngine?.let { engine.setSpeechRecognizerEngine(it) }
        translationEngine?.let { engine.setTranslationEngine(it) }
        ttsEngine?.let { engine.setTextToSpeechEngine(it) }
        return engine
    }
}
