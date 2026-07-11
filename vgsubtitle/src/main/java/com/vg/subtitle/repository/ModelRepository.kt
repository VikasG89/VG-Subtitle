package com.vg.subtitle.repository

import android.content.Context
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.database.entity.ModelEntity
import kotlinx.coroutines.flow.Flow

class ModelRepository(context: Context) {
    private val modelDao = VGSubtitleDatabase.get(context).modelDao()

    fun observeModels(): Flow<List<ModelEntity>> {
        return modelDao.observeAll()
    }

    suspend fun getModel(modelId: String): ModelEntity? {
        return modelDao.getModel(modelId)
    }

    suspend fun markAsDownloaded(modelId: String, path: String) {
        modelDao.updateDownloadStatus(modelId, true, path)
    }
}
