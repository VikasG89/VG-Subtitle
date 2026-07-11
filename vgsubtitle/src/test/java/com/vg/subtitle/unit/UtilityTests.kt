package com.vg.subtitle.unit

import com.vg.subtitle.subtitle.TimestampGenerator
import com.vg.subtitle.translator.LanguageMapper
import com.vg.subtitle.utils.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UtilityTests {

    @Test
    fun testTimePercent() {
        assertEquals(50, TimeUtils.percent(500, 1000))
        assertEquals(0, TimeUtils.percent(0, 1000))
        assertEquals(100, TimeUtils.percent(1000, 1000))
        assertEquals(100, TimeUtils.percent(1200, 1000))
    }

    @Test
    fun testEtaEstimation() {
        assertEquals(1000L, TimeUtils.estimateEta(500, 1000, 1000))
        assertEquals(0L, TimeUtils.estimateEta(1000, 1000, 2000))
    }

    @Test
    fun testTimestampFormatting() {
        assertEquals("01:02:03,456", TimestampGenerator.toSrt(3_723_456L))
    }

    @Test
    fun testLanguageMapping() {
        assertEquals("hi", LanguageMapper.normalize("Hindi"))
        assertEquals("zh", LanguageMapper.normalize("Chinese"))
        assertTrue(LanguageMapper.supportedLanguages().contains("mr"))
    }
}
