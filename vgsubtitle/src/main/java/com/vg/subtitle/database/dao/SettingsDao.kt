package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.SettingsEntity

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: SettingsEntity)

    @Query("SELECT * FROM settings WHERE `key` = :key")
    suspend fun get(key: String): SettingsEntity?

    @Query("SELECT * FROM settings")
    suspend fun getAll(): List<SettingsEntity>
}
