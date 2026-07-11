# Kotlin Integration Guide

```kotlin
val config = SubtitleConfig(
    sourceLanguage = "auto",
    targetLanguages = listOf("hi", "mr", "ta"),
    model = WhisperModel.BASE,
)

val engine = VGSubtitleEngine(context, config)
    .setSpeechRecognizerEngine(myOfflineRecognizer)
    .setTranslationEngine(myOfflineTranslator)
    .setTextToSpeechEngine(myOfflineTts)

engine.generateSubtitle(videoPath, outputPath, listener)
engine.translateSubtitle(srtPath, outputDir, "en", listOf("hi", "mr"), translationListener)
engine.generateTranslatedAudio(srtPath, outputDir, "en", listOf("hi"), audioListener)
```

Use `SubtitleWorker.enqueue(context, videoPath, outputPath)` for queued background generation when the default engine bindings are available in the app process.
