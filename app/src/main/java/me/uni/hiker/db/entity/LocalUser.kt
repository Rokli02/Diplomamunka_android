package me.uni.hiker.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "local_user",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true),
    ]
)
data class LocalUser(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "remote_id") var remoteId: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_DATE") val createdAt: LocalDate,
    @ColumnInfo(name = "is_active", defaultValue = "true") val isActive: Boolean,
)