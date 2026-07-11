package com.vg.subtitle.repository

import android.content.Context
import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.SubtitleProgress
import com.vg.subtitle.api.model.WhisperModel
import com.vg.subtitle.audio.AudioGenerator
import com.vg.subtitle.audio.AudioSynchronizer
import com.vg.subtitle.audio.TextToSpeechEngine
import com.vg.subtitle.extractor.VideoAudioExtractor
import com.vg.subtitle.model.manager.ModelManager
import com.vg.subtitle.api.model.Segment
import com.vg.subtitle.recognizer.SpeechRecognizerEngine
import com.vg.subtitle.recognizer.WhisperEngine
import com.vg.subtitle.subtitle.SrtParser
import com.vg.subtitle.subtitle.SubtitleFormatter
import com.vg.subtitle.translator.MlKitTranslationEngine
import com.vg.subtitle.translator.SubtitleTranslator
import com.vg.subtitle.translator.TranslationEngine
import com.vg.subtitle.utils.FileUtils
import com.vg.subtitle.utils.TimeUtils
import java.io.File
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive

/**
 * Central repository for handling subtitle, translation, and audio generation logic.
 *
 * This class orchestrates various components like [VideoAudioExtractor], [SpeechRecognizerEngine],
 * and [TranslationEngine] to perform the end-to-end processing of media files.
 *
 * @property context The application context.
 * @property extractor Helper for extracting audio tracks from video files.
 * @property modelManager Manages Whisper model files on disk.
 * @property recognizer The engine used for speech-to-text transcription.
 * @property translationEngine The engine used for text-to-text translation.
 * @property ttsEngine Optional engine for generating AI-voiced audio (TTS).
 */
class SubtitleRepository(
    private val context: Context,
    private val extractor: VideoAudioExtractor = VideoAudioExtractor(),
    private val modelManager: ModelManager = ModelManager(context.applicationContext),
    private var recognizer: SpeechRecognizerEngine = WhisperEngine(modelManager),
    private var translationEngine: TranslationEngine = MlKitTranslationEngine(),
    private var ttsEngine: TextToSpeechEngine? = null,
) {
    private val formatter = SubtitleFormatter()
    private val parser = SrtParser()
    @Volatile private var paused = false
    @Volatile private var cancelled = false

    /** Sets the speech recognizer engine. */
    fun setSpeechRecognizerEngine(engine: SpeechRecognizerEngine) {
        recognizer = engine
    }

    /** Sets the translation engine. */
    fun setTranslationEngine(engine: TranslationEngine) {
        translationEngine = engine
    }

    /** Sets the text-to-speech engine. */
    fun setTextToSpeechEngine(engine: TextToSpeechEngine) {
        ttsEngine = engine
    }

    /** Checks if the specified model is downloaded and ready. */
    fun isModelAvailable(model: WhisperModel): Boolean {
        return modelManager.isModelAvailable(model)
    }

    /** Ensures the model is available, or throws an exception. */
    fun ensureModelAvailable(model: WhisperModel): File {
        return modelManager.requireModel(model)
    }

    /** Registers a model file from an external source. */
    fun registerModel(model: WhisperModel, source: File): File {
        return modelManager.registerModel(model, source)
    }

    /**
     * Extracts audio from video and transcribes it into a subtitle file.
     *
     * @param videoPath Path to the input video.
     * @param outputPath Path where the generated subtitle will be saved.
     * @param config Configuration for transcription.
     * @param onProgress Progress update callback.
     * @param onSegment Callback for each new transcribed segment.
     * @return The generated subtitle [File].
     */
    suspend fun generateSubtitle(
        videoPath: String,
        outputPath: String,
        config: SubtitleConfig,
        onProgress: (SubtitleProgress) -> Unit,
        onSegment: (Segment) -> Unit,
    ): File {
        resetRunState()
        val video = FileUtils.requireReadable(videoPath)
        recognizer.setModel(config.model)
        val wav = FileUtils.tempFile(context, "speech_", ".wav")
        waitIfPaused()
        extractor.extractToWav(video, wav, onProgress)
        checkActive()
        val durationMs = extractor.durationMs(video)

        onProgress(
            SubtitleProgress(
                percent = 45,
                processedMs = 0L,
                totalMs = durationMs,
                currentTimestampMs = 0L,
                etaMs = 0L,
                message = "Transcribing audio...",
            ),
        )

        val segments = mutableListOf<Segment>()
        val started = System.currentTimeMillis()
        recognizer.transcribeStreaming(wav, config).collect { segment ->
            checkActive()
            waitIfPaused()
            segments += segment
            onSegment(segment)
            onProgress(
                SubtitleProgress(
                    percent = TimeUtils.percent(segment.endMs, durationMs),
                    processedMs = segment.endMs,
                    totalMs = durationMs,
                    currentTimestampMs = segment.endMs,
                    etaMs = TimeUtils.estimateEta(segment.endMs, durationMs, System.currentTimeMillis() - started),
                    message = "Recognizing speech",
                ),
            )
        }
        val output = FileUtils.ensureParent(outputPath)
        output.writeText(formatter.normalizeAndFormat(segments, config), Charsets.UTF_8)
        onProgress(SubtitleProgress(100, durationMs, durationMs, durationMs, 0L, "Subtitle generated"))
        return output
    }

    /**
     * Translates an existing subtitle file into one or more target languages.
     *
     * @param subtitlePath Path to the source SRT file.
     * @param outputDirectory Directory to save translated files.
     * @param sourceLanguage Source language code or "auto".
     * @param targetLanguages List of target language codes.
     * @param config Configuration for translation.
     * @param onProgress Progress update callback.
     * @param onSegment Callback for each translated segment.
     * @return Map of language code to generated [File].
     */
    suspend fun translateSubtitle(
        subtitlePath: String,
        outputDirectory: String,
        sourceLanguage: String,
        targetLanguages: List<String>,
        config: SubtitleConfig,
        onProgress: (SubtitleProgress) -> Unit,
        onSegment: (String, Segment) -> Unit,
    ): Map<String, File> {
        resetRunState()
        val source = FileUtils.requireReadable(subtitlePath)
        val segments = parser.parse(source)
        val actualSource = if (sourceLanguage == "auto") {
            segments.firstOrNull { it.language != "und" }?.language ?: "en"
        } else sourceLanguage
        val translated = SubtitleTranslator(translationEngine).translate(segments, actualSource, targetLanguages)
        val outputDir = File(outputDirectory).apply { mkdirs() }
        val outputs = mutableMapOf<String, File>()
        var completed = 0
        val total = translated.values.sumOf { it.size }.coerceAtLeast(1)
        for ((language, languageSegments) in translated) {
            waitIfPaused()
            checkActive()
            languageSegments.forEach {
                completed += 1
                onSegment(language, it)
                onProgress(
                    SubtitleProgress(
                        percent = completed * 100 / total,
                        processedMs = completed.toLong(),
                        totalMs = total.toLong(),
                        currentTimestampMs = it.endMs,
                        etaMs = 0L,
                        message = "Translating $language",
                    ),
                )
            }
            val output = File(outputDir, "${source.nameWithoutExtension}_$language.${config.outputFormat.extension}")
            output.writeText(formatter.format(languageSegments, config.outputFormat), Charsets.UTF_8)
            outputs[language] = output
        }
        onProgress(SubtitleProgress(100, total.toLong(), total.toLong(), 0L, 0L, "Translation completed"))
        return outputs
    }

    /**
     * Generates translated audio (dubbing) for a subtitle.
     *
     * @param subtitlePath Path to the source SRT file.
     * @param outputDirectory Directory to save audio files.
     * @param sourceLanguage Source language code.
     * @param targetLanguages Target language codes.
     * @param config Configuration.
     * @param onProgress Progress update callback.
     * @param onAudioSegment Callback for each generated audio segment.
     * @return Map of language code to audio folder/file.
     */
    suspend fun generateTranslatedAudio(
        subtitlePath: String,
        outputDirectory: String,
        sourceLanguage: String,
        targetLanguages: List<String>,
        config: SubtitleConfig,
        onProgress: (SubtitleProgress) -> Unit,
        onAudioSegment: (String, String, Long, Long) -> Unit,
    ): Map<String, File> {
        val engine = ttsEngine ?: throw IllegalStateException("No offline TTS engine is configured.")
        val translations = translateSubtitle(subtitlePath, outputDirectory, sourceLanguage, targetLanguages, config, onProgress) { _, _ -> }
        val generator = AudioGenerator(engine)
        val synchronizer = AudioSynchronizer()
        val outputs = mutableMapOf<String, File>()
        for ((language, translatedSubtitle) in translations) {
            val segments = parser.parse(translatedSubtitle)
            val audioFiles = generator.generate(segments, language, File(outputDirectory, "audio_$language"))
            synchronizer.align(segments, audioFiles).forEach {
                onAudioSegment(language, it.audio.absolutePath, it.segment.startMs, it.segment.endMs)
            }
            outputs[language] = File(outputDirectory, "audio_$language")
        }
        return outputs
    }

    /** Pauses the current operation. */
    fun pause() {
        paused = true
    }

    /** Resumes a paused operation. */
    fun resume() {
        paused = false
    }

    /** Cancels the current operation. */
    fun cancel() {
        cancelled = true
    }

    private fun resetRunState() {
        paused = false
        cancelled = false
    }

    private suspend fun waitIfPaused() {
        while (paused) {
            checkActive()
            delay(250L)
        }
    }

    private suspend fun checkActive() {
        if (cancelled) throw CancellationException("Task was cancelled.")
        currentCoroutineContext().ensureActive()
    }
}
