package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exports")
data class ExportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val videoPath: String,
    val subtitleId: Long,
    val exportPath: String,
    val format: String,
    val status: String,
    val createdAtMs: Long = System.currentTimeMillis()
)
