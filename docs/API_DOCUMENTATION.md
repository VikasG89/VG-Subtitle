# API Documentation

## `VGSubtitle`

- `generateSubtitle(videoPath, outputPath, listener, config)`: extracts audio, transcribes offline speech, formats subtitles, and writes the requested subtitle output.
- `translateSubtitle(subtitlePath, outputDirectory, sourceLanguage, targetLanguages, listener, config)`: translates subtitle text while preserving timestamps.
- `generateTranslatedAudio(subtitlePath, outputDirectory, sourceLanguage, targetLanguages, listener, config)`: translates subtitle text and synthesizes timestamped audio segments using the configured TTS engine.
- `pause()`, `resume()`, `cancel()`, `getProgress()`: task controls.

## Engine Interfaces

- `SpeechRecognizerEngine`: implement for Whisper, ONNX, or other local ASR.
- `TranslationEngine`: implement for local neural or rules-based translation.
- `TextToSpeechEngine`: implement for Android TTS, local neural TTS, or future voice cloning.
- `VideoMuxer` and `AudioMuxer`: implement with Media3 Transformer, FFmpeg, or a native media stack.

## Exceptions

The API reports descriptive failures with subclasses of `VGSubtitleException`, including unsupported codec, corrupted video, storage, low-memory, recognition, translation, and audio generation failures.
