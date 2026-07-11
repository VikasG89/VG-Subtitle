package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.ExportEntity

@Dao
interface ExportDao {
    @Insert
    suspend fun insert(export: ExportEntity)

    @Query("SELECT * FROM exports WHERE videoPath = :videoPath")
    suspend fun getExportsForVideo(videoPath: String): List<ExportEntity>
}
