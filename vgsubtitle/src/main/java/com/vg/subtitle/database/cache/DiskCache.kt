package com.vg.subtitle.database.cache

import android.content.Context
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.database.entity.CacheEntity
import java.io.File

class DiskCache(private val context: Context) {
    private val db = VGSubtitleDatabase.get(context)
    private val cacheDao = db.cacheDao()
    private val cacheDir = File(context.cacheDir, "vg_subtitle_cache").apply { mkdirs() }

    suspend fun put(key: String, data: ByteArray, contentType: String) {
        val file = File(cacheDir, key)
        file.writeBytes(data)
        val entity = CacheEntity(
            key = key,
            filePath = file.absolutePath,
            size = data.size.toLong(),
            contentType = contentType
        )
        cacheDao.insert(entity)
    }

    suspend fun get(key: String): ByteArray? {
        val entity = cacheDao.get(key) ?: return null
        val file = File(entity.filePath)
        if (!file.exists()) {
            cacheDao.delete(key)
            return null
        }
        return file.readBytes()
    }

    suspend fun remove(key: String) {
        val entity = cacheDao.get(key)
        if (entity != null) {
            File(entity.filePath).delete()
            cacheDao.delete(key)
        }
    }
}
