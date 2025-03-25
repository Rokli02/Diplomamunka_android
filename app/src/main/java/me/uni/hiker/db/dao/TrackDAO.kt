package me.uni.hiker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.uni.hiker.db.entity.Track

@Dao
interface TrackDAO {
    @Query("SELECT * FROM track WHERE (CASE WHEN :userId IS NULL THEN user_id IS NULL ELSE user_id = :userId OR user_id IS NULL END) AND (lat BETWEEN :minLat AND :maxLat) AND (lon BETWEEN :minLon AND :maxLon)")
    suspend fun findAll(minLat: Double, maxLat: Double, minLon: Double, maxLon: Double, userId: Long?): List<Track>

    @Query("SELECT * FROM track WHERE id = :id AND (CASE WHEN :userId IS NULL THEN user_id IS NULL ELSE user_id = :userId OR user_id IS NULL END)")
    suspend fun findById(id: Long, userId: Long?): Track?

    @Update
    suspend fun updateOne(track: Track)

    @Insert
    suspend fun insertOne(track: Track): Long

    @Delete
    suspend fun deleteOne(track: Track)
}