package com.vg.subtitle.database.cache

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class CleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Implement logic to clean up old cache entries and diagnostics
        return Result.success()
    }
}

object CleanupScheduler {
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<CleanupWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "cache_cleanup",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
