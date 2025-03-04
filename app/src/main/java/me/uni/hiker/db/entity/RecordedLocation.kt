package me.uni.hiker.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "recorded_location")
data class RecordedLocation(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
)
