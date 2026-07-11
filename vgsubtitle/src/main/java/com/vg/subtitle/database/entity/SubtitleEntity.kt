package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subtitles",
    foreignKeys = [
        ForeignKey(
            entity = VideoEntity::class,
            parentColumns = ["videoPath"],
            childColumns = ["videoPath"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["videoPath"])]
)
data class SubtitleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val videoPath: String,
    val language: String,
    val format: String,
    val content: String, // Or path to file if content is large
    val isOriginal: Boolean = false,
    val createdAtMs: Long = System.currentTimeMillis()
)
