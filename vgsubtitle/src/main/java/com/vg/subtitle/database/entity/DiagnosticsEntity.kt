package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostics")
data class DiagnosticsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val tag: String,
    val message: String,
    val level: Int, // e.g., INFO=2, WARN=3, ERROR=4
    val timestampMs: Long = System.currentTimeMillis(),
    val metadata: String? = null // JSON blob for additional info
)
