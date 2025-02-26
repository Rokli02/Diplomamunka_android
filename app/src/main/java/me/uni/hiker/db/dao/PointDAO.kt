package me.uni.hiker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.uni.hiker.db.entity.Point

@Dao
interface PointDAO {
    @Query("SELECT * FROM point WHERE track_id = :trackId")
    suspend fun findAllByTrack(trackId: Long): List<Point>

    @Insert
    suspend fun insertAll(list: List<Point>)

    @Insert
    suspend fun insertOne(point: Point)
}