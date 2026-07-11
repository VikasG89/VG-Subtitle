package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.SessionEntity

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)

    @Query("UPDATE sessions SET endTimeMs = :endTime WHERE sessionId = :sessionId")
    suspend fun endSession(sessionId: String, endTime: Long = System.currentTimeMillis())

    @Query("SELECT * FROM sessions ORDER BY startTimeMs DESC LIMIT 1")
    suspend fun getCurrentSession(): SessionEntity?
}
