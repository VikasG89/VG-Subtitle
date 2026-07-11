# Performance Tuning

- Keep extraction streaming. Do not copy whole videos or decoded audio into memory.
- Use `SubtitleConfig.chunkDurationMs` between 15 and 45 seconds for long 4K/8K videos.
- Prefer Tiny/Base models on low-RAM devices and Small/Medium/Large only when the device has enough memory.
- Persist partial transcript state every chunk in the concrete recognizer to support resume after cancellation or process death.
- Run transcription on `Dispatchers.IO` or a bounded native worker pool.
- Emit progress at roughly one-second intervals to avoid notification churn.
- Store temporary WAV/PCM chunks under app cache and delete them after successful output generation.
- Use WorkManager for queued jobs and a foreground service for visible long-running processing.
