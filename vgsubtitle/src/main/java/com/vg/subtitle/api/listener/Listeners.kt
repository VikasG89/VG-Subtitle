package com.vg.subtitle.api.listener

import androidx.annotation.Keep
import com.vg.subtitle.api.exception.VGSubtitleException
import com.vg.subtitle.api.model.SubtitleProgress
import com.vg.subtitle.api.model.Segment

/** 
 * Receives subtitle generation events. 
 * All callbacks are safe to implement from Java. 
 */
@Keep
interface SubtitleListener {
    /** Called when the transcription progress is updated. */
    fun onProgress(progress: SubtitleProgress) = Unit
    /** Called when a new subtitle segment is generated. */
    fun onSubtitle(segment: Segment) = Unit
    /** Called when the subtitle generation is successfully completed. */
    fun onCompleted(outputPath: String) = Unit
    /** Called when the task was cancelled. */
    fun onCancelled() = Unit
    /** Called when an error occurs during subtitle generation. */
    fun onError(error: VGSubtitleException) = Unit
}

/** 
 * Receives subtitle translation events. 
 */
@Keep
interface TranslationListener {
    /** Called when the translation progress is updated. */
    fun onProgress(progress: SubtitleProgress) = Unit
    /** Called when a new translated segment is available. */
    fun onTranslatedSubtitle(language: String, segment: Segment) = Unit
    /** Called when all requested translations are completed. */
    fun onCompleted(outputs: Map<String, String>) = Unit
    /** Called when the translation task was cancelled. */
    fun onCancelled() = Unit
    /** Called when an error occurs during translation. */
    fun onError(error: VGSubtitleException) = Unit
}

/** 
 * Receives translated audio generation (dubbing) events. 
 */
@Keep
interface AudioTranslationListener {
    /** Called when the audio generation progress is updated. */
    fun onProgress(progress: SubtitleProgress) = Unit
    /** Called when a new translated audio segment is generated. */
    fun onAudioSegment(language: String, path: String, startMs: Long, endMs: Long) = Unit
    /** Called when all requested audio files are generated. */
    fun onCompleted(outputs: Map<String, String>) = Unit
    /** Called when the audio generation task was cancelled. */
    fun onCancelled() = Unit
    /** Called when an error occurs during audio generation. */
    fun onError(error: VGSubtitleException) = Unit
}
