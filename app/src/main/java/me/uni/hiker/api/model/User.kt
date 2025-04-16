package me.uni.hiker.api.model

import me.uni.hiker.model.user.User

data class LoginResponse(val token: String, val user: User)

typealias SignUpResponse = User