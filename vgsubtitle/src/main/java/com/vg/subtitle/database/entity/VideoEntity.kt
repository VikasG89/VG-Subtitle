package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val videoPath: String,
    val fileName: String,
    val durationMs: Long,
    val fileSize: Long,
    val mimeType: String,
    val lastAccessedMs: Long = System.currentTimeMillis(),
    val fingerprint: String? = null
)
