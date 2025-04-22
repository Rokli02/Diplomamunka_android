package me.uni.hiker.model.user

import me.uni.hiker.db.entity.LocalUser
import java.time.LocalDate

data class NewUser(
    var name: String,
    var username: String,
    var email: String,
    var password: String,
) {
    fun toEntity(remoteId: String? = null): LocalUser {
        return LocalUser(
            remoteId = remoteId,
            name = this.name,
            username = this.username,
            email = this.email,
            password = this.password,
            isActive = true,
            createdAt = LocalDate.now(),
        )
    }
}
