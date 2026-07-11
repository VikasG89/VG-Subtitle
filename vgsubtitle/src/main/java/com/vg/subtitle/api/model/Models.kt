package com.vg.subtitle.api.model

import androidx.annotation.Keep

/**
 * Represents the available OpenAI Whisper model sizes.
 *
 * Larger models generally provide higher accuracy but require more memory and processing power.
 */
@Keep
enum class WhisperModel { 
    /** Tiny model (~75MB). Fastest, lowest accuracy. */
    TINY, 
    /** Base model (~145MB). Good balance for simple tasks. */
    BASE, 
    /** Small model (~480MB). Recommended for mobile production. */
    SMALL, 
    /** Medium model (~1.5GB). High accuracy, slower. */
    MEDIUM, 
    /** Large model (~3GB). State-of-the-art accuracy, very slow on mobile. */
    LARGE 
}

/**
 * Supported subtitle and transcript output formats.
 */
@Keep
enum class SubtitleFormat(val extension: String) {
    /** SubRip Subtitle format. */
    SRT("srt"),
    /** Web Video Text Tracks format. */
    WEBVTT("vtt"),
    /** Advanced Substation Alpha format. */
    ASS("ass"),
    /** Substation Alpha format. */
    SSA("ssa"),
    /** Plain text transcript. */
    TEXT("txt"),
    /** Structured JSON format. */
    JSON("json"),
}

/**
 * Represents the current execution state of a background task.
 */
@Keep
enum class TaskState { 
    /** Task is waiting in the queue. */
    QUEUED, 
    /** Task is currently processing. */
    RUNNING, 
    /** Task is temporarily suspended. */
    PAUSED, 
    /** Task finished successfully. */
    COMPLETED, 
    /** Task was manually aborted. */
    CANCELLED, 
    /** Task stopped due to an error. */
    FAILED 
}
