package me.uni.hiker.utils

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Cache<K, V>(private val expirationTimeSec: Long) {
    private val cache = ConcurrentHashMap<K, Pair<V, LocalDateTime>>()
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {

        scheduler.scheduleWithFixedDelay(this::cleanupExpiredEntries, expirationTimeSec, expirationTimeSec, TimeUnit.SECONDS)
    }

    operator fun set(key: K, value: V) {
        cache[key] = Pair(value, LocalDateTime.now().plusSeconds((expirationTimeSec)))
    }

    operator fun get(key: K): V? {
        val entry = cache[key]
        return if (entry != null && entry.second.isAfter(LocalDateTime.now())) {
            entry.first
        } else {
            cache.remove(key) // Remove if expired
            null
        }
    }

    fun remove(key: K) {
        cache.remove(key)
    }

    private fun cleanupExpiredEntries() {
        val now = LocalDateTime.now()
        cache.entries.removeIf { it.value.second.isBefore(now) }
    }

    fun shutdown() {
        scheduler.shutdown()
    }

}