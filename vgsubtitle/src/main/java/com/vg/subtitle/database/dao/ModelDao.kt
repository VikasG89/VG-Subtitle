package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.ModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelEntity)

    @Query("SELECT * FROM ai_models")
    fun observeAll(): Flow<List<ModelEntity>>

    @Query("SELECT * FROM ai_models WHERE modelId = :modelId")
    suspend fun getModel(modelId: String): ModelEntity?

    @Query("UPDATE ai_models SET isDownloaded = :downloaded, filePath = :path WHERE modelId = :modelId")
    suspend fun updateDownloadStatus(modelId: String, downloaded: Boolean, path: String?)
}
