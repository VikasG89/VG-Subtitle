package com.vg.subtitle.model.manager

import android.content.Context
import com.vg.subtitle.api.model.WhisperModel
import java.io.File

/** Manages local Whisper model files. */
class ModelManager(private val context: Context) {
    fun isModelAvailable(model: WhisperModel): Boolean {
        return getModelFile(model).exists()
    }

    fun requireModel(model: WhisperModel): File {
        val file = getModelFile(model)
        if (!file.exists()) {
            throw IllegalStateException("Model ${model.name} not found at ${file.absolutePath}")
        }
        return file
    }

    fun registerModel(model: WhisperModel, source: File): File {
        val target = getModelFile(model)
        source.copyTo(target, overwrite = true)
        return target
    }

    fun getModelFile(model: WhisperModel): File {
        return File(context.filesDir, "models/whisper_${model.name.lowercase()}.bin")
    }
}
