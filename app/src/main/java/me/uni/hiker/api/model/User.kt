package me.uni.hiker.api.model

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class RemoteUser(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val createdAt: Date,
    val rights: Int,
)

data class LoginResponse(val token: String, val user: RemoteUser)

typealias SignUpResponse = RemoteUser