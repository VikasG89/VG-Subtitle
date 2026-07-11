package com.vg.subtitle.repository

import android.content.Context
import com.vg.subtitle.database.cache.DiskCache
import com.vg.subtitle.database.cache.MemoryCache

class CacheRepository(context: Context) {
    private val diskCache = DiskCache(context)
    private val memoryCache = MemoryCache<String, ByteArray>(100) // 100 entries

    suspend fun get(key: String): ByteArray? {
        memoryCache.get(key)?.let { return it }
        val diskData = diskCache.get(key)
        if (diskData != null) {
            memoryCache.put(key, diskData)
        }
        return diskData
    }

    suspend fun put(key: String, data: ByteArray, contentType: String) {
        memoryCache.put(key, data)
        diskCache.put(key, data, contentType)
    }
}
