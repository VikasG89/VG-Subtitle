package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.CacheEntity

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cache: CacheEntity)

    @Query("SELECT * FROM cache_metadata WHERE `key` = :key")
    suspend fun get(key: String): CacheEntity?

    @Query("DELETE FROM cache_metadata WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("SELECT SUM(size) FROM cache_metadata")
    suspend fun getTotalSize(): Long

    @Query("SELECT * FROM cache_metadata ORDER BY lastAccessedMs ASC LIMIT :limit")
    suspend fun getOldest(limit: Int): List<CacheEntity>
}
