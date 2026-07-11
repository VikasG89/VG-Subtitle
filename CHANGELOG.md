# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-07-11

### Added
- Core `VGSubtitleEngine` with Java-compatible API.
- Whisper.cpp integration via JNI bridge.
- Silero VAD integration for speech detection.
- Offline translation using Google ML Kit.
- Room-based persistence for tasks and diagnostics.
- Multi-tier caching system.
- CI/CD pipeline with GitHub Actions.
- Maven publishing configuration.
- KDoc and Doxygen documentation for all major components.
- Security policy and contribution guidelines.

### Changed
- Refactored engine to use repository pattern for better testability.
- Updated to NDK 28.2.13676358.

### Fixed
- Fixed concurrency issues in transcription progress reporting.
- Resolved JNI memory leaks in audio decoder.
