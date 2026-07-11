# Java Integration Guide

```java
SubtitleConfig config = new SubtitleConfig(
    "auto",
    java.util.Arrays.asList("hi", "mr"),
    WhisperModel.BASE,
    SubtitleFormat.SRT,
    false,
    true,
    true,
    42,
    7000L,
    900L,
    1000L,
    30000L,
    16000,
    1,
    16
);

VGSubtitle engine = new VGSubtitle(context, config);
engine.setSpeechRecognizerEngine(
    WhisperEngines.fromBlockingBridge(context, myBlockingWhisperBridge)
);
engine.setTranslationEngine(myOfflineTranslator);

engine.generateSubtitle(videoPath, outputPath, new SubtitleListener() {
    @Override public void onProgress(SubtitleProgress progress) {}
    @Override public void onSubtitle(Segment segment) {}
    @Override public void onCompleted(String outputPath) {}
    @Override public void onCancelled() {}
    @Override public void onError(VGSubtitleException error) {}
});
```

Speech recognition requirement:

```java
public final class MyBlockingWhisperBridge implements BlockingNativeWhisperBridge {
    @Override public void loadModel(String modelPath) {
        // Load whisper.cpp, ONNX, ML Kit offline pack, or another local ASR model.
    }

    @Override public boolean isModelLoaded() {
        return true;
    }

    @Override public String detectLanguage(String pcmPath) {
        return "en";
    }

    @Override public List<Segment> transcribe(String pcmPath, long chunkDurationMs) {
        // Return real subtitle segments from your native/local ASR runtime.
        return Collections.emptyList();
    }
}
```

Controls:

```java
engine.pause();
engine.resume();
engine.cancel();
SubtitleProgress progress = engine.getProgress();
```
