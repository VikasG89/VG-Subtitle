package com.vg.subtitle.recognizer

import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.api.model.WhisperModel
import java.io.File
import kotlinx.coroutines.flow.Flow

/** Pluggable offline speech recognition engine. */
interface SpeechRecognizerEngine {
    suspend fun setModel(model: WhisperModel)
    suspend fun detectLanguage(audioPcm16kMono: File): String
    fun transcribeStreaming(audioPcm16kMono: File, config: SubtitleConfig): Flow<Segment>
    suspend fun transcribeBatch(audioPcm16kMono: File, config: SubtitleConfig): List<Segment>
    suspend fun resumeTranscription(audioPcm16kMono: File, config: SubtitleConfig, fromMs: Long): List<Segment>
}
