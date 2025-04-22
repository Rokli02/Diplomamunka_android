package me.uni.hiker.model.user

import androidx.compose.runtime.Immutable
import me.uni.hiker.db.entity.LocalUser
import java.time.LocalDate

@Immutable
data class User(
    val id: Long,
    val remoteId: String? = null,
    val name: String,
    val username: String,
    val email: String,
    var token: String? = null,
    val createdAt: LocalDate,
) {
    companion object {
        fun fromEntity(entity: LocalUser): User {
            return User(
                id = entity.id,
                remoteId = entity.remoteId,
                name = entity.name,
                username = entity.username,
                email = entity.email,
                createdAt = entity.createdAt,
            )
        }
    }
}
