# VG Subtitle & Audio AI Engine

Production-oriented Android library module for offline subtitle generation, translation, and AI-driven audio processing in VG Player.

## Key Features

- **Multi-Engine Transcription**: Native JNI bridge for Whisper.cpp with support for custom inference engines.
- **Voice Activity Detection (VAD)**: Integration of Silero VAD for high-precision speech segmentation before transcription.
- **Offline Translation**: Pluggable translation engines, including ML Kit offline packs and ONNX models.
- **AI Audio Generation**: Text-to-Speech (TTS) integration for generating translated audio tracks synchronized with subtitles.
- **Advanced Persistence**: Robust Room-based storage for task persistence, video metadata, AI model management, and session diagnostics.
- **Intelligent Caching**: Multi-tier cache (Memory + Disk) with content fingerprinting to avoid redundant processing.
- **Production Ready**: Support for WorkManager, foreground services, pause/resume/cancel state management, and detailed performance diagnostics.
- **Cross-Platform Compatibility**: Kotlin-first implementation with a clean, fully-compatible Java API.

## Java API

```java
VGSubtitleEngine engine = new VGSubtitleEngine(context);

engine.generateSubtitle(
    videoPath,
    outputPath,
    new SubtitleListener() {
        @Override public void onProgress(SubtitleProgress progress) { /* update UI */ }
        @Override public void onSubtitle(Segment segment) { /* live subtitle stream */ }
        @Override public void onCompleted(String outputPath) { /* done */ }
        @Override public void onError(VGSubtitleException error) { /* handle error */ }
    }
);
```

## Kotlin API

```kotlin
val engine = VGSubtitleEngine(context)
engine.generateSubtitle(videoPath, outputPath, object : SubtitleListener {
    override fun onProgress(progress: SubtitleProgress) { /* ... */ }
    override fun onSubtitle(segment: Segment) { /* ... */ }
    override fun onCompleted(path: String) { /* ... */ }
    override fun onError(e: VGSubtitleException) { /* ... */ }
})
```

## Advanced Components

### Voice Activity Detection (VAD)
Configure high-precision speech detection before sending audio to Whisper:
```kotlin
val vadConfig = NativeVadConfig(speechThreshold = 0.5f, minSpeechDurationMs = 250)
val sessionHandle = NativeVadBridge.initializeVad(vadConfig)
// ... process audio ...
NativeVadBridge.releaseVad(sessionHandle)
```

### Persistence & Repositories
Manage models and video metadata efficiently:
```kotlin
val modelRepo = ModelRepository(context)
val models = modelRepo.observeModels() // Returns Flow<List<ModelEntity>>

val videoRepo = VideoRepository(context)
val video = videoRepo.getVideo(path)
```

## Integration Requirements

- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 37 (Android 15)
- **NDK Version**: 28.2.13676358
- **C++ Standard**: C++17

## Build Instructions

```bash
# Run JVM unit tests
./gradlew :vgsubtitle:testDebugUnitTest

# Assemble the AAR library
./gradlew :vgsubtitle:assembleDebug
```

Native Whisper, VAD, and Translation runtimes are injectable. Commercial apps can bind proprietary engines or optimized GGML/ONNX runtimes without modifying the core library architecture.
