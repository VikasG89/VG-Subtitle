package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * JNI bridge for FFmpeg-based audio extraction.
 */
@Keep
object NativeAudioExtractor {

    init {
        NativeLibraryLoader.load()
    }

    /**
     * Extracts audio from a video file and saves it as a 16kHz mono WAV file.
     * @param videoPath Path to the input video file.
     * @param outputPath Path where the extracted WAV should be saved.
     * @return 0 on success, negative error code on failure.
     */
    external fun extractToWav(
        videoPath: String,
        outputPath: String,
        callback: ExtractionCallback?
    ): Int

    @Keep
    interface ExtractionCallback {
        fun onProgress(percent: Int)
    }
}
