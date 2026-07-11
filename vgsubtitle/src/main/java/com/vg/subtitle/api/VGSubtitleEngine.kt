package com.vg.subtitle.api

import android.content.Context
import androidx.annotation.Keep
import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.exception.*
import com.vg.subtitle.api.listener.AudioTranslationListener
import com.vg.subtitle.api.listener.SubtitleListener
import com.vg.subtitle.api.listener.TranslationListener
import com.vg.subtitle.api.model.SubtitleProgress
import com.vg.subtitle.api.model.WhisperModel
import com.vg.subtitle.audio.TextToSpeechEngine
import com.vg.subtitle.recognizer.SpeechRecognizerEngine
import com.vg.subtitle.repository.SubtitleRepository
import com.vg.subtitle.translator.TranslationEngine
import java.io.File
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Java-compatible facade for offline subtitle, translation, and translated audio tasks.
 *
 * This engine serves as the main entry point for the VG Subtitle library, providing
 * high-level APIs for transcription, translation, and text-to-speech tasks.
 *
 * @property context The application context.
 * @property defaultConfig The default configuration to use for tasks if none is provided.
 */
@Keep
class VGSubtitleEngine @JvmOverloads constructor(
    context: Context,
    private var defaultConfig: SubtitleConfig = SubtitleConfig(),
) {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val repository = SubtitleRepository(appContext)
    private val progress = AtomicReference(SubtitleProgress(0, 0, 0, 0, 0))
    @Volatile private var activeJob: Job? = null

    /**
     * Sets the custom speech recognizer engine.
     * @param engine The engine implementation to use for transcription.
     * @return This engine instance for chaining.
     */
    fun setSpeechRecognizerEngine(engine: SpeechRecognizerEngine): VGSubtitleEngine = apply {
        repository.setSpeechRecognizerEngine(engine)
    }

    /**
     * Sets the custom translation engine.
     * @param engine The engine implementation to use for translation.
     * @return This engine instance for chaining.
     */
    fun setTranslationEngine(engine: TranslationEngine): VGSubtitleEngine = apply {
        repository.setTranslationEngine(engine)
    }

    /**
     * Sets the custom text-to-speech engine.
     * @param engine The engine implementation to use for audio generation.
     * @return This engine instance for chaining.
     */
    fun setTextToSpeechEngine(engine: TextToSpeechEngine): VGSubtitleEngine = apply {
        repository.setTextToSpeechEngine(engine)
    }

    /**
     * Updates the default configuration.
     * @param config The new default configuration.
     * @return This engine instance for chaining.
     */
    fun setDefaultConfig(config: SubtitleConfig): VGSubtitleEngine = apply {
        defaultConfig = config
    }

    /**
     * Checks if a specific Whisper model is available locally.
     * @param model The model to check. Defaults to the one in [defaultConfig].
     * @return True if the model file exists and is valid.
     */
    @JvmOverloads
    fun isModelAvailable(model: WhisperModel = defaultConfig.model): Boolean {
        return repository.isModelAvailable(model)
    }

    /**
     * Ensures that a Whisper model is available, potentially downloading or copying it.
     * @param model The model to ensure. Defaults to the one in [defaultConfig].
     * @return The absolute path to the model file.
     * @throws ModelException if the model cannot be made available.
     */
    @JvmOverloads
    fun ensureModelAvailable(model: WhisperModel = defaultConfig.model): String {
        return repository.ensureModelAvailable(model).absolutePath
    }

    /**
     * Registers a pre-downloaded model file.
     * @param model The Whisper model type.
     * @param sourcePath The path to the model file on disk.
     * @return The absolute path where the model was registered.
     */
    fun registerModel(model: WhisperModel, sourcePath: String): String {
        return repository.registerModel(model, File(sourcePath)).absolutePath
    }

    /**
     * Generates subtitles for a video file.
     * @param videoPath Absolute path to the input video/audio file.
     * @param outputPath Absolute path where the .srt subtitle will be saved.
     * @param listener Callback for progress and results.
     * @param config Optional configuration override.
     */
    @JvmOverloads
    fun generateSubtitle(videoPath: String, outputPath: String, listener: SubtitleListener, config: SubtitleConfig = defaultConfig) {
        activeJob = scope.launch {
            try {
                val output = repository.generateSubtitle(
                    videoPath = videoPath,
                    outputPath = outputPath,
                    config = config,
                    onProgress = {
                        progress.set(it)
                        listener.onProgress(it)
                    },
                    onSegment = listener::onSubtitle,
                )
                listener.onCompleted(output.absolutePath)
            } catch (cancelled: CancellationException) {
                listener.onCancelled()
            } catch (error: VGSubtitleException) {
                listener.onError(error)
            } catch (error: Throwable) {
                listener.onError(VGSubtitleException(error.message ?: "Subtitle generation failed.", error))
            }
        }
    }

    /**
     * Translates an existing subtitle file into multiple languages.
     * @param subtitlePath Path to the source .srt file.
     * @param outputDirectory Directory where translated .srt files will be saved.
     * @param sourceLanguage ISO 639-1 language code of the source subtitle.
     * @param targetLanguages List of ISO 639-1 codes to translate into.
     * @param listener Callback for progress and results.
     * @param config Optional configuration override.
     */
    @JvmOverloads
    fun translateSubtitle(
        subtitlePath: String,
        outputDirectory: String,
        sourceLanguage: String,
        targetLanguages: List<String>,
        listener: TranslationListener,
        config: SubtitleConfig = defaultConfig,
    ) {
        activeJob = scope.launch {
            try {
                val outputs = repository.translateSubtitle(
                    subtitlePath,
                    outputDirectory,
                    sourceLanguage,
                    targetLanguages,
                    config,
                    onProgress = {
                        progress.set(it)
                        listener.onProgress(it)
                    },
                    onSegment = listener::onTranslatedSubtitle,
                ).mapValues { it.value.absolutePath }
                listener.onCompleted(outputs)
            } catch (cancelled: CancellationException) {
                listener.onCancelled()
            } catch (error: VGSubtitleException) {
                listener.onError(error)
            } catch (error: Throwable) {
                listener.onError(TranslationException(error.message ?: "Subtitle translation failed.", error))
            }
        }
    }

    /**
     * Transcribes a video and then translates the result into multiple languages.
     * @param videoPath Path to the input video/audio file.
     * @param outputDirectory Directory to save all generated subtitle files.
     * @param sourceLanguage ISO 639-1 code or "auto" for detection.
     * @param targetLanguages List of ISO 639-1 codes to translate into.
     * @param listener Callback for progress and results.
     * @param config Optional configuration override.
     */
    @JvmOverloads
    fun generateAndTranslateSubtitle(
        videoPath: String,
        outputDirectory: String,
        sourceLanguage: String = "auto",
        targetLanguages: List<String>,
        listener: TranslationListener,
        config: SubtitleConfig = defaultConfig
    ) {
        activeJob = scope.launch {
            try {
                val tempSrt = File.createTempFile("subtitle_", ".srt", appContext.cacheDir)
                repository.generateSubtitle(
                    videoPath = videoPath,
                    outputPath = tempSrt.absolutePath,
                    config = config.copy(sourceLanguage = sourceLanguage),
                    onProgress = {
                        progress.set(it)
                        listener.onProgress(it.copy(message = "Transcribing: ${it.message}"))
                    },
                    onSegment = { }
                )

                val outputs = repository.translateSubtitle(
                    subtitlePath = tempSrt.absolutePath,
                    outputDirectory = outputDirectory,
                    sourceLanguage = sourceLanguage,
                    targetLanguages = targetLanguages,
                    config = config,
                    onProgress = {
                        progress.set(it)
                        listener.onProgress(it.copy(message = "Translating: ${it.message}"))
                    },
                    onSegment = listener::onTranslatedSubtitle,
                ).mapValues { it.value.absolutePath }

                tempSrt.delete()
                listener.onCompleted(outputs)
            } catch (cancelled: CancellationException) {
                listener.onCancelled()
            } catch (error: VGSubtitleException) {
                listener.onError(error)
            } catch (error: Throwable) {
                listener.onError(TranslationException(error.message ?: "Task failed.", error))
            }
        }
    }

    /**
     * Generates translated audio (dubbing) for a given subtitle.
     * @param subtitlePath Path to the source .srt file.
     * @param outputDirectory Directory to save generated audio files.
     * @param sourceLanguage Source language code.
     * @param targetLanguages Target language codes for dubbing.
     * @param listener Callback for progress and results.
     * @param config Optional configuration override.
     */
    @JvmOverloads
    fun generateTranslatedAudio(
        subtitlePath: String,
        outputDirectory: String,
        sourceLanguage: String,
        targetLanguages: List<String>,
        listener: AudioTranslationListener,
        config: SubtitleConfig = defaultConfig,
    ) {
        activeJob = scope.launch {
            try {
                val outputs = repository.generateTranslatedAudio(
                    subtitlePath,
                    outputDirectory,
                    sourceLanguage,
                    targetLanguages,
                    config,
                    onProgress = {
                        progress.set(it)
                        listener.onProgress(it)
                    },
                    onAudioSegment = listener::onAudioSegment,
                ).mapValues { it.value.absolutePath }
                listener.onCompleted(outputs)
            } catch (cancelled: CancellationException) {
                listener.onCancelled()
            } catch (error: VGSubtitleException) {
                listener.onError(error)
            } catch (error: Throwable) {
                listener.onError(AudioGenerationException(error.message ?: "Translated audio generation failed.", error))
            }
        }
    }

    /** Pauses the current active task if supported. */
    fun pause() {
        repository.pause()
    }

    /** Resumes the current paused task if supported. */
    fun resume() {
        repository.resume()
    }

    /** Cancels the current active task. */
    fun cancel() {
        repository.cancel()
        activeJob?.cancel()
    }

    /** Returns the current progress of the active task. */
    fun getProgress(): SubtitleProgress = progress.get()
}
