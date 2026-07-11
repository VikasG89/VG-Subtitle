package com.vg.subtitle.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.vg.subtitle.api.config.SubtitleConfig
import com.vg.subtitle.api.model.TaskState
import com.vg.subtitle.database.SubtitleTask
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.repository.SubtitleRepository
import java.util.UUID
import java.util.concurrent.TimeUnit

class SubtitleWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val video = inputData.getString(KEY_VIDEO) ?: return Result.failure()
        val output = inputData.getString(KEY_OUTPUT) ?: return Result.failure()
        val taskId = inputData.getString(KEY_TASK_ID) ?: UUID.randomUUID().toString()
        val dao = VGSubtitleDatabase.get(applicationContext).taskDao()
        return runCatching {
            dao.upsert(SubtitleTask(taskId, video, output, TaskState.RUNNING))
            SubtitleRepository(applicationContext).generateSubtitle(video, output,
                SubtitleConfig(), onProgress = {
                setProgressAsync(Data.Builder().putInt(KEY_PROGRESS, it.percent).build())
            }, onSegment = {})
            dao.updateState(taskId, TaskState.COMPLETED, 100, System.currentTimeMillis(), null)
            Result.success()
        }.getOrElse {
            dao.updateState(taskId, TaskState.FAILED, 0, System.currentTimeMillis(), it.message)
            Result.retry()
        }
    }

    companion object {
        const val KEY_TASK_ID = "task_id"
        const val KEY_VIDEO = "video_path"
        const val KEY_OUTPUT = "output_path"
        const val KEY_PROGRESS = "progress"

        @JvmStatic
        fun enqueue(context: Context, videoPath: String, outputPath: String): UUID {
            val id = UUID.randomUUID()
            val request = OneTimeWorkRequestBuilder<SubtitleWorker>()
                .setId(id)
                .setInputData(
                    Data.Builder()
                        .putString(KEY_TASK_ID, id.toString())
                        .putString(KEY_VIDEO, videoPath)
                        .putString(KEY_OUTPUT, outputPath)
                        .build(),
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(id.toString(), ExistingWorkPolicy.APPEND_OR_REPLACE, request)
            return id
        }
    }
}

class TranslationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val subtitle = inputData.getString(KEY_SUBTITLE) ?: return Result.failure()
        val outputDir = inputData.getString(KEY_OUTPUT_DIR) ?: return Result.failure()
        val sourceLanguage = inputData.getString(KEY_SOURCE_LANGUAGE) ?: "auto"
        val targets = inputData.getStringArray(KEY_TARGETS)?.toList().orEmpty()
        return runCatching {
            SubtitleRepository(applicationContext).translateSubtitle(
                subtitle,
                outputDir,
                sourceLanguage,
                targets,
                SubtitleConfig(targetLanguages = targets),
                onProgress = { setProgressAsync(Data.Builder().putInt(SubtitleWorker.KEY_PROGRESS, it.percent).build()) },
                onSegment = { _, _ -> },
            )
            Result.success()
        }.getOrElse { Result.retry() }
    }

    companion object {
        const val KEY_SUBTITLE = "subtitle_path"
        const val KEY_OUTPUT_DIR = "output_dir"
        const val KEY_SOURCE_LANGUAGE = "source_language"
        const val KEY_TARGETS = "target_languages"
    }
}

class AudioWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val subtitle = inputData.getString(TranslationWorker.KEY_SUBTITLE) ?: return Result.failure()
        val outputDir = inputData.getString(TranslationWorker.KEY_OUTPUT_DIR) ?: return Result.failure()
        val sourceLanguage = inputData.getString(TranslationWorker.KEY_SOURCE_LANGUAGE) ?: "auto"
        val targets = inputData.getStringArray(TranslationWorker.KEY_TARGETS)?.toList().orEmpty()
        return runCatching {
            SubtitleRepository(applicationContext).generateTranslatedAudio(
                subtitle,
                outputDir,
                sourceLanguage,
                targets,
                SubtitleConfig(targetLanguages = targets, generateTranslatedAudio = true),
                onProgress = { setProgressAsync(Data.Builder().putInt(SubtitleWorker.KEY_PROGRESS, it.percent).build()) },
                onAudioSegment = { _, _, _, _ -> },
            )
            Result.success()
        }.getOrElse { Result.retry() }
    }
}
