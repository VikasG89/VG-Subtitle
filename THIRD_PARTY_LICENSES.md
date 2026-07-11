# Third-Party Licenses

This project uses several open-source and third-party libraries. Below is a summary of their licenses.

## Android / Kotlin Dependencies

### AndroidX Libraries
- **License**: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- **Includes**: `core-ktx`, `lifecycle-runtime-ktx`, `work-runtime-ktx`, `room-runtime`, `room-ktx`.

### Kotlinx Coroutines
- **License**: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- **Includes**: `kotlinx-coroutines-play-services`, `kotlinx-coroutines-test`.

### Google ML Kit (Translate)
- **License**: [Google ML Kit Terms of Service](https://developers.google.com/ml-kit/terms)
- **Note**: Requires attribution in the application's "About" or "Legal" section.

### JUnit
- **License**: [Eclipse Public License 1.0](https://www.eclipse.org/legal/epl-v10.html)

## Native Dependencies

### Whisper.cpp
- **License**: [MIT License](https://github.com/ggerganov/whisper.cpp/blob/master/LICENSE)
- **Description**: Port of OpenAI's Whisper model in C/C++.

### Silero VAD
- **License**: [MIT License](https://github.com/snakers4/silero-vad/blob/master/LICENSE)
- **Description**: Pre-trained enterprise-grade Voice Activity Detector (VAD).

### FFmpeg (Optional Integration)
- **License**: [LGPL v2.1+ / GPL v2+](https://ffmpeg.org/legal.html)
- **Note**: The core library provides wrappers for FFmpeg. Users must ensure compliance with FFmpeg's licensing if they link against it (e.g., using `Mobile-FFmpeg` or `FFmpeg-Kit`).
