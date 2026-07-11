package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.SubtitleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtitleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subtitle: SubtitleEntity)

    @Query("SELECT * FROM subtitles WHERE videoPath = :videoPath")
    fun getSubtitlesForVideo(videoPath: String): Flow<List<SubtitleEntity>>

    @Query("SELECT * FROM subtitles WHERE id = :id")
    suspend fun getSubtitle(id: Long): SubtitleEntity?

    @Delete
    suspend fun delete(subtitle: SubtitleEntity)
}
