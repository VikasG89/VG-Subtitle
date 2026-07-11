package com.vg.subtitle.unit

import com.vg.subtitle.api.config.SubtitleConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class ConfigTests {

    @Test
    fun testDefaultConfig() {
        val config = SubtitleConfig()
        assertEquals("auto", config.sourceLanguage)
        assertEquals(16000, config.sampleRateHz)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidSampleRate() {
        SubtitleConfig(sampleRateHz = 44100)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidLineLength() {
        SubtitleConfig(maxSubtitleLineLength = 10)
    }

    @Test
    fun testNormalizedLanguage() {
        val config = SubtitleConfig(sourceLanguage = "EN-US")
        assertEquals("en-us", config.normalizedSourceLanguage())
    }
}
