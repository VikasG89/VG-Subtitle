package com.vg.subtitle.database

import android.content.Context
import androidx.room.*
import com.vg.subtitle.api.model.TaskState
import com.vg.subtitle.database.dao.*
import com.vg.subtitle.database.entity.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "subtitle_tasks")
data class SubtitleTask(
    @PrimaryKey val id: String,
    val videoPath: String,
    val outputPath: String,
    val state: TaskState,
    val progressPercent: Int = 0,
    val createdAtMs: Long = System.currentTimeMillis(),
    val updatedAtMs: Long = System.currentTimeMillis(),
    val errorMessage: String? = null,
)

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: SubtitleTask)

    @Query("SELECT * FROM subtitle_tasks WHERE id = :id")
    suspend fun get(id: String): SubtitleTask?

    @Query("SELECT * FROM subtitle_tasks ORDER BY createdAtMs ASC")
    fun observeAll(): Flow<List<SubtitleTask>>

    @Query("UPDATE subtitle_tasks SET state = :state, progressPercent = :progress, updatedAtMs = :updatedAtMs, errorMessage = :error WHERE id = :id")
    suspend fun updateState(id: String, state: TaskState, progress: Int, updatedAtMs: Long = System.currentTimeMillis(), error: String? = null)

    @Query("DELETE FROM subtitle_tasks WHERE id = :id")
    suspend fun delete(id: String)
}

@Database(
    entities = [
        SubtitleTask::class,
        VideoEntity::class,
        SubtitleEntity::class,
        CacheEntity::class,
        ModelEntity::class,
        LanguageEntity::class,
        ExportEntity::class,
        SessionEntity::class,
        DiagnosticsEntity::class,
        SettingsEntity::class
    ],
    version = 2, // Upgraded version
    exportSchema = false
)
abstract class VGSubtitleDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun videoDao(): VideoDao
    abstract fun subtitleDao(): SubtitleDao
    abstract fun cacheDao(): CacheDao
    abstract fun modelDao(): ModelDao
    abstract fun exportDao(): ExportDao
    abstract fun sessionDao(): SessionDao
    abstract fun diagnosticsDao(): DiagnosticsDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile private var instance: VGSubtitleDatabase? = null

        @JvmStatic
        fun get(context: Context): VGSubtitleDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    VGSubtitleDatabase::class.java,
                    "vgsubtitle.db",
                )
                .fallbackToDestructiveMigration() // For development, simplify migrations
                .build().also { instance = it }
            }
        }
    }
}
