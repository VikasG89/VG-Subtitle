package com.vg.subtitle.database.cache

import android.util.LruCache

class MemoryCache<K : Any, V : Any>(maxSize: Int) {
    private val cache = LruCache<K, V>(maxSize)

    fun put(key: K, value: V) {
        cache.put(key, value)
    }

    fun get(key: K): V? {
        return cache.get(key)
    }

    fun remove(key: K) {
        cache.remove(key)
    }

    fun clear() {
        cache.evictAll()
    }
}
