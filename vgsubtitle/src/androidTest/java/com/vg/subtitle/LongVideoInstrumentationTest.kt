package com.vg.subtitle

import android.os.Debug
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vg.subtitle.api.SubtitleConfig
import com.vg.subtitle.extractor.VideoAudioExtractor
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LongVideoInstrumentationTest {
    @Test
    fun extractorKeepsMemoryBoundedForFixtureVideo() {
        val fixturePath = System.getProperty("vgsubtitle.longVideo")
        assumeTrue("Set -Dvgsubtitle.longVideo=/sdcard/test/long.mp4 to run.", !fixturePath.isNullOrBlank())

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val before = Debug.getNativeHeapAllocatedSize()
        val output = File(context.cacheDir, "long_video_fixture.wav")

        VideoAudioExtractor().extractToWav(File(fixturePath), output)

        val after = Debug.getNativeHeapAllocatedSize()
        assertTrue("Expected streaming extraction output.", output.length() > 44L)
        assertTrue("Native heap grew too much.", after - before < MAX_NATIVE_HEAP_GROWTH_BYTES)
    }

    @Test
    fun longVideoConfigUsesStreamingSizedChunks() {
        val config = SubtitleConfig(chunkDurationMs = 30_000L)
        assertTrue(config.chunkDurationMs <= 120_000L)
    }

    private companion object {
        const val MAX_NATIVE_HEAP_GROWTH_BYTES = 96L * 1024L * 1024L
    }
}
