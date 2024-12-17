package com.core.network

import com.core.network.model.AuthResponse
import com.core.network.model.LoginRequest
import com.core.network.model.PodcastListResponse
import com.core.network.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/podcasts")
    suspend fun GetPodcastListByQuery(
        @Query("query") q: String
    ): PodcastListResponse

    @GET("/podcasts/featured")
    suspend fun GetPodcastListFeatured(): PodcastListResponse

    @GET("/podcasts/popular")
    suspend fun GetPodcastListPopular(): PodcastListResponse

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

}