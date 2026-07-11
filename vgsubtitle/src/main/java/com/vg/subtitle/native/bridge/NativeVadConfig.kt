package com.vg.subtitle.native.bridge

import androidx.annotation.Keep

/**
 * Configuration for Silero Voice Activity Detection (VAD).
 */
@Keep
data class NativeVadConfig(
    val speechThreshold: Float = 0.5f,
    val minSpeechDurationMs: Int = 250,
    val minSilenceDurationMs: Int = 100,
    val prePaddingMs: Int = 32,
    val postPaddingMs: Int = 32,
    val maxSegmentLengthMs: Int = 30000,
    val segmentOverlapMs: Int = 0,
    val confidenceThreshold: Float = 0.5f,
    val sampleRate: Int = 16000
)
