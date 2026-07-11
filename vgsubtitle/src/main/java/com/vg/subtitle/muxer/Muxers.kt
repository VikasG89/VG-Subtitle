package com.vg.subtitle.muxer

import java.io.File

interface AudioMuxer {
    suspend fun muxAudioTracks(originalVideo: File, audioTracks: List<File>, outputVideo: File): File
}

interface VideoMuxer {
    suspend fun muxSubtitle(video: File, subtitle: File, outputVideo: File): File
}
