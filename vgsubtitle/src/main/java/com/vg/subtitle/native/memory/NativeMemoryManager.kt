package com.vg.subtitle.native.memory

import androidx.annotation.Keep
import android.os.Debug

/**
 * Monitors and manages native memory usage.
 */
@Keep
object NativeMemoryManager {
    /** Returns total native memory used by the process in bytes. */
    fun getNativeHeapAllocatedSize(): Long {
        return Debug.getNativeHeapAllocatedSize()
    }

    /** Returns native memory limit if applicable. */
    fun getNativeHeapSize(): Long {
        return Debug.getNativeHeapSize()
    }
}
