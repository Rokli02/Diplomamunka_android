package me.uni.hiker.api.service

import me.uni.hiker.api.model.HealthcheckResponse
import retrofit2.Response
import retrofit2.http.GET

interface CommonService {
    @GET("healthcheck")
    suspend fun healthcheck(): Response<HealthcheckResponse>
}