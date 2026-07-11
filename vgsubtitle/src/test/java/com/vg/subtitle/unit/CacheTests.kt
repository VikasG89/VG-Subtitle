package com.vg.subtitle.unit

import com.vg.subtitle.database.cache.MemoryCache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CacheTests {

    @Test
    fun testMemoryCachePutAndGet() {
        val cache = MemoryCache<String, String>(10)
        cache.put("key1", "value1")
        assertEquals("value1", cache.get("key1"))
    }

    @Test
    fun testMemoryCacheEviction() {
        val cache = MemoryCache<String, String>(2)
        cache.put("1", "a")
        cache.put("2", "b")
        cache.put("3", "c") // This should evict "1"
        
        assertNull(cache.get("1"))
        assertEquals("b", cache.get("2"))
        assertEquals("c", cache.get("3"))
    }

    @Test
    fun testMemoryCacheClear() {
        val cache = MemoryCache<String, String>(10)
        cache.put("k", "v")
        cache.clear()
        assertNull(cache.get("k"))
    }
}
