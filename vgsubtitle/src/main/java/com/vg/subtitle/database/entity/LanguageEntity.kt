package com.vg.subtitle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val code: String, // ISO code
    val name: String,
    val nativeName: String?,
    val isSupportedForTranscription: Boolean = false,
    val isSupportedForTranslation: Boolean = false,
    val isSupportedForTts: Boolean = false
)
