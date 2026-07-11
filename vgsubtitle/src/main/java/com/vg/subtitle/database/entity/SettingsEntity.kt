package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val key: String,
    val value: String,
    val type: String // "String", "Int", "Boolean", "Long", "Float"
)
