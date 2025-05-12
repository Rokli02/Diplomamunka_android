package me.uni.hiker.api.model

import androidx.compose.runtime.Immutable
import me.uni.hiker.db.entity.LocalUser
import java.util.Date

@Immutable
data class RemoteUser(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val createdAt: Date,
    val rights: Int,
) {
    fun compareToLocalUser(user: LocalUser): Int {
        var res = 0

        if (user.name != this.name) res = res or 0b1
        if (user.username != this.username)  res = res or 0b10
        if (user.email != this.email)  res = res or 0b100

        return res
    }
}

data class LoginResponse(val token: String, val user: RemoteUser)

typealias SignUpResponse = RemoteUser