package com.vg.subtitle.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vg.subtitle.database.entity.VideoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private lateinit var db: VGSubtitleDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VGSubtitleDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testVideoInsertAndRetrieve() = runBlocking {
        val video = VideoEntity(
            videoPath = "/path/to/video.mp4",
            fileName = "video.mp4",
            durationMs = 10000,
            fileSize = 1024,
            mimeType = "video/mp4"
        )
        db.videoDao().insert(video)
        val retrieved = db.videoDao().getVideo("/path/to/video.mp4")
        assertEquals("video.mp4", retrieved?.fileName)
    }
}
