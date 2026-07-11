# Benchmark Plan

## Unit Benchmarks

- Timestamp formatting throughput.
- SRT parse/write throughput for 10k subtitle entries.
- Segment overlap normalization for dense speech.

## Instrumentation Benchmarks

- 30-minute, 2-hour, and 5-hour fixture videos.
- 1080p, 4K, and 8K containers with AAC, MP3, AC3, EAC3, FLAC, Opus, Vorbis, and PCM audio tracks.
- Native heap growth during extraction, transcription, translation, and TTS.
- Battery and thermal behavior while running through WorkManager plus foreground notification.

## Pass Criteria

- No full-video byte arrays or decoded full-audio buffers.
- Progress callback cadence near one second.
- Extraction heap growth stays under 96 MB for long-video fixtures.
- Subtitle timestamps remain monotonic and non-overlapping.
