package com.core.network

import com.core.common.annotations.Authorized
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
//auth
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

//podcasts
    @Authorized
    @GET("podcasts/get-featured")
    suspend fun GetPodcastListFeatured(): PodcastListResponse

    @Authorized
    @GET("podcasts/get-popular")
    suspend fun GetPodcastListPopular(): PodcastListResponse

//episodes
//    @Authorized
//   @GET("podcasts/get-featured")
//    suspend fun SearchEpisodes(): PodcastListResponse
}