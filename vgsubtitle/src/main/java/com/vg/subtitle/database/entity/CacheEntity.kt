package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_metadata")
data class CacheEntity(
    @PrimaryKey val key: String,
    val filePath: String,
    val size: Long,
    val contentType: String,
    val createdAtMs: Long = System.currentTimeMillis(),
    val lastAccessedMs: Long = System.currentTimeMillis(),
    val expirationMs: Long? = null
)
