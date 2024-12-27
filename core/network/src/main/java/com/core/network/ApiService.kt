package com.core.network

import com.core.common.constants.HeaderName
import com.core.common.constants.HeaderValue
import com.core.network.model.podcasts.PodcastDetailedResponse
import com.core.network.model.podcasts.PodcastListResponse
import com.core.network.model.user.AuthResponse
import com.core.network.model.user.LoginRequest
import com.core.network.model.user.PurchasePremiumRequest
import com.core.network.model.user.PurchasePremiumResponse
import com.core.network.model.user.RegisterRequest
import com.core.network.model.user.UpdateUserResponse
import com.core.network.model.user.UserFullResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.random.Random

interface ApiService {
    //auth
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}",
        "Cache-Control: no-cache, no-store, must-revalidate",
        "Pragma: no-cache"
    )
    @GET("auth/get-user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserFullResponse>

    @Headers(
        "Accept: application/json"
    )
    @Multipart
    @POST("auth/update-user")
    @JvmSuppressWildcards
    suspend fun updateUser(
        @PartMap params: Map<String, RequestBody>,
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<UpdateUserResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/purchase-premium")
    suspend fun purchasePremium(
        @Body purchasePremiumRequest: PurchasePremiumRequest, @Header("Authorization") token: String
    ): Response<PurchasePremiumResponse>

    //podcasts
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-featured")
    suspend fun getPodcastListFeatured(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-popular")
    suspend fun getPodcastListPopular(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-new")
    suspend fun getPodcastListNew(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/{podcast-id}")
    suspend fun getPodcastFull(
        @Path("podcast-id") podcastId: Int, @Header("Authorization") token: String
    ): Response<PodcastDetailedResponse>

    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/{podcast-id}/add-to-saved")
    suspend fun addToSavedPodcast(
        @Path("podcast-id") podcastId: Int, @Header("Authorization") token: String
    ): Response<Unit>
}
