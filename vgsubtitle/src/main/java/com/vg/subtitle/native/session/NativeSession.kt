package com.vg.subtitle.native.session

import androidx.annotation.Keep
import com.vg.subtitle.native.bridge.NativeBridge
import java.io.Closeable
import java.util.concurrent.atomic.AtomicLong

/**
 * Manages a native inference session lifecycle.
 * Kotlin owns the engine/session lifecycle.
 */
@Keep
class NativeSession(private val modelPath: String) : Closeable {
    private val contextPtr = AtomicLong(0L)

    fun initialize() {
        if (contextPtr.get() == 0L) {
            val ptr = NativeBridge.initialize(modelPath)
            if (ptr == 0L) {
                throw RuntimeException("Failed to initialize native session")
            }
            contextPtr.set(ptr)
        }
    }

    fun getPtr(): Long = contextPtr.get()

    fun isInitialized(): Boolean = contextPtr.get() != 0L

    override fun close() {
        val ptr = contextPtr.getAndSet(0L)
        if (ptr != 0L) {
            NativeBridge.release(ptr)
        }
    }

    protected fun finalize() {
        close()
    }
}
