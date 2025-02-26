package me.uni.hiker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.uni.hiker.db.entity.LocalUser

@Dao
interface LocalUserDAO {
    @Query("SELECT * FROM local_user WHERE username = :username AND is_active = :isActive")
    suspend fun findByUsername(username: String, isActive: Boolean = true): LocalUser?

    @Query("SELECT * FROM local_user WHERE email = :email AND is_active = :isActive")
    suspend fun findByEmail(email: String, isActive: Boolean = true): LocalUser?

    @Query("UPDATE local_user SET is_active = 0 WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun insertOne(user: LocalUser)
}