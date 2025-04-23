package me.uni.hiker.api.service

import me.uni.hiker.api.model.GetAllResponse
import me.uni.hiker.api.model.GetByBoundariesResponse
import me.uni.hiker.api.model.GetByIdResponse
import me.uni.hiker.api.model.SaveRequestBody
import me.uni.hiker.api.model.SaveResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackService {
    @GET("tracks")
    suspend fun getAll(
        @Query("p") page: Int? = null,
        @Query("ps") pageSize: Int? = null,
        @Query("f") filter: String? = null,
    ): Response<GetAllResponse>

    @GET("tracks/{id}")
    suspend fun getById(@Path("id") externalId: String): Response<GetByIdResponse>

    @GET("tracks/lat/{minLat}/{maxLat}/lon/{minLon}/{maxLon}")
    suspend fun getByBoundaries(
        @Path("minLat") minLat: Double,
        @Path("maxLat") maxLat: Double,
        @Path("minLon") minLon: Double,
        @Path("maxLon") maxLon: Double,
        @Query("cdd") clusterDistanceDivisor: Int?,
    ): Response<GetByBoundariesResponse>

    @POST("tracks")
    suspend fun save(@Body track: SaveRequestBody): Response<SaveResponse>
}