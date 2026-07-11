package com.vg.subtitle.database.diagnostics

import android.content.Context
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.database.entity.DiagnosticsEntity
import java.util.UUID

class DiagnosticsManager(context: Context) {
    private val diagnosticsDao = VGSubtitleDatabase.get(context).diagnosticsDao()
    private val sessionId = UUID.randomUUID().toString()

    suspend fun log(tag: String, message: String, level: Int = 2, metadata: String? = null) {
        diagnosticsDao.insert(
            DiagnosticsEntity(
                sessionId = sessionId,
                tag = tag,
                message = message,
                level = level,
                metadata = metadata
            )
        )
    }

    suspend fun getSessionLogs(): List<DiagnosticsEntity> {
        return diagnosticsDao.getForSession(sessionId)
    }
}
