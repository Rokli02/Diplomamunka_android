package me.uni.hiker.api.service

import me.uni.hiker.api.model.LoginResponse
import me.uni.hiker.api.model.SignUpResponse
import me.uni.hiker.model.user.Login
import me.uni.hiker.model.user.NewUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("users/login")
    suspend fun login(@Body loginUser: Login): Response<LoginResponse>

    @POST("users/sign-up")
    suspend fun signUp(@Body newUser: NewUser): Response<SignUpResponse>
}