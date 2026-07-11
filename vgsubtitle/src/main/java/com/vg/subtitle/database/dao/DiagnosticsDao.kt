package com.vg.subtitle.database.dao

import androidx.room.*
import com.vg.subtitle.database.entity.DiagnosticsEntity

@Dao
interface DiagnosticsDao {
    @Insert
    suspend fun insert(diagnostics: DiagnosticsEntity)

    @Query("SELECT * FROM diagnostics WHERE sessionId = :sessionId")
    suspend fun getForSession(sessionId: String): List<DiagnosticsEntity>

    @Query("DELETE FROM diagnostics WHERE timestampMs < :threshold")
    suspend fun clearOldDiagnostics(threshold: Long)
}
