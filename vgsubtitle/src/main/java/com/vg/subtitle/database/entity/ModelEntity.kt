package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_models")
data class ModelEntity(
    @PrimaryKey val modelId: String,
    val modelName: String,
    val type: String, // e.g., "WHISPER", "TRANSLATION", "TTS", "VAD"
    val version: String,
    val filePath: String?,
    val isDownloaded: Boolean = false,
    val sizeBytes: Long = 0,
    val lastUsedMs: Long = System.currentTimeMillis()
)
