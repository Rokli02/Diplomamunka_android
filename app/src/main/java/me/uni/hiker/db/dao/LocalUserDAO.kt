package me.uni.hiker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.uni.hiker.db.entity.LocalUser

@Dao
interface LocalUserDAO {
    @Query("SELECT * FROM local_user WHERE (email = :emailOrUsername OR username = :emailOrUsername) AND is_active = :isActive")
    suspend fun findByEmailOrUsername(emailOrUsername: String, isActive: Boolean? = true): LocalUser?

    @Query("SELECT * FROM local_user WHERE email = :email OR username = :username LIMIT 1")
    suspend fun isExistsByEmailOrUsername(email: String, username: String): LocalUser?

    @Query("UPDATE local_user SET is_active = 0 WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun insertOne(user: LocalUser)
}