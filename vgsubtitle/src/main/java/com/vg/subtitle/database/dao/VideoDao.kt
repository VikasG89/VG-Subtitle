package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: VideoEntity)

    @Update
    suspend fun update(video: VideoEntity)

    @Query("SELECT * FROM videos WHERE videoPath = :path")
    suspend fun getVideo(path: String): VideoEntity?

    @Query("SELECT * FROM videos ORDER BY lastAccessedMs DESC")
    fun observeAll(): Flow<List<VideoEntity>>

    @Delete
    suspend fun delete(video: VideoEntity)
}
