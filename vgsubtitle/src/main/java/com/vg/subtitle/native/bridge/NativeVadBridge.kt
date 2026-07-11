package com.vg.subtitle.native.bridge

import androidx.annotation.Keep
import com.vg.subtitle.native.loader.NativeLibraryLoader

/**
 * JNI bridge for Silero VAD integration.
 * Defined in VAD integration blueprint.
 */
@Keep
object NativeVadBridge {

    init {
        NativeLibraryLoader.load()
    }

    /**
     * Initializes a new VAD session with the given configuration.
     * Returns a handle (pointer) to the native session.
     */
    external fun initializeVad(config: NativeVadConfig): Long

    /**
     * Releases the native VAD session.
     */
    external fun releaseVad(sessionHandle: Long)

    /**
     * Processes incremental PCM audio data and returns detected segments.
     */
    external fun processAudio(
        sessionHandle: Long,
        audioData: FloatArray
    ): Array<NativeVadSegment>?

    /**
     * Resets the VAD session state.
     */
    external fun resetVad(sessionHandle: Long)

    /**
     * Finalizes processing and returns any remaining segments.
     */
    external fun flushVad(sessionHandle: Long): Array<NativeVadSegment>?

    /**
     * Returns diagnostic statistics for the VAD engine.
     */
    external fun getVadStats(): Map<String, String>
}

/**
 * Represents a speech segment detected by the native VAD engine.
 */
@Keep
data class NativeVadSegment(
    val startMs: Long,
    val endMs: Long,
    val confidence: Float
)
