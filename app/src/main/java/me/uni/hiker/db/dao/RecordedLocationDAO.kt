package me.uni.hiker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.uni.hiker.db.entity.RecordedLocation

@Dao
interface RecordedLocationDAO {
    @Insert
    suspend fun insertOne(recordedLocation: RecordedLocation)

    @Query("DELETE FROM recorded_location")
    suspend fun prune()

    @Query("SELECT * FROM recorded_location ORDER BY id DESC LIMIT 1")
    fun getLastInsertedFlow(): Flow<RecordedLocation?>

    @Query("SELECT * FROM recorded_location ORDER BY id DESC LIMIT 1")
    suspend fun getLastInserted(): RecordedLocation?

    @Query("SELECT * FROM recorded_location ORDER BY id ASC")
    suspend fun getAll(): List<RecordedLocation>

    @Query("SELECT * FROM recorded_location ORDER BY id DESC LIMIT :x")
    suspend fun getLastX(x: Int): List<RecordedLocation>
}