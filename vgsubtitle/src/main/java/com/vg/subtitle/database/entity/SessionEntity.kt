package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val sessionId: String,
    val startTimeMs: Long = System.currentTimeMillis(),
    val endTimeMs: Long? = null,
    val deviceModel: String?,
    val osVersion: String?,
    val appVersion: String?
)
