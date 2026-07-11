package com.vg.subtitle.repository

import android.content.Context
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.database.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

class VideoRepository(context: Context) {
    private val videoDao = VGSubtitleDatabase.get(context).videoDao()

    suspend fun addVideo(video: VideoEntity) {
        videoDao.insert(video)
    }

    suspend fun getVideo(path: String): VideoEntity? {
        return videoDao.getVideo(path)
    }

    fun observeVideos(): Flow<List<VideoEntity>> {
        return videoDao.observeAll()
    }
}
