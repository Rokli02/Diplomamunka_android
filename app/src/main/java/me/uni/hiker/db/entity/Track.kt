package me.uni.hiker.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "track",
    foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Track(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "remote_id") val remoteId: String?,
    @ColumnInfo(name = "user_id") val userId: String?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "length", defaultValue = "0.0") val length: Float,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_DATE") val createdAt: LocalDate?,
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_DATE") val updatedAt: LocalDate?,
)
