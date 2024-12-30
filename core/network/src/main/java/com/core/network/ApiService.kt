package com.core.network

import com.core.common.constants.HeaderName
import com.core.common.constants.HeaderValue
import com.core.network.model.episodes.EpisodeDetailedResponse
import com.core.network.model.episodes.EpisodeFullDTO
import com.core.network.model.podcasts.CategoryResponseData
import com.core.network.model.podcasts.GuestResponseData
import com.core.network.model.podcasts.PodcastDetailedResponse
import com.core.network.model.podcasts.PodcastListResponse
import com.core.network.model.podcasts.SearchEpisodesData
import com.core.network.model.podcasts.TopicResponseData
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
import retrofit2.http.QueryMap
import kotlin.random.Random

interface ApiService {
    /**
     * User. Login
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    /**
     * User. Register
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    /**
     * User. Get user
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}",
        "Cache-Control: no-cache, no-store, must-revalidate",
        "Pragma: no-cache"
    )
    @GET("auth/get-user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserFullResponse>

    /**
     * User. Update user with multipart
     * */
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

    /**
     * User. Purchase premium
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @POST("auth/purchase-premium")
    suspend fun purchasePremium(
        @Body purchasePremiumRequest: PurchasePremiumRequest, @Header("Authorization") token: String
    ): Response<PurchasePremiumResponse>

    /**
     * Podcast. List of featured
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-featured")
    suspend fun getPodcastListFeatured(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    /**
     * Podcast. List of popular
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-popular")
    suspend fun getPodcastListPopular(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    /**
     * Podcast. List of new
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/get-new")
    suspend fun getPodcastListNew(
        @Query("page") page: Int, @Header("Authorization") token: String
    ): Response<PodcastListResponse>

    /**
     * Podcast. Detailed single
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/{podcast-id}")
    suspend fun getPodcastFull(
        @Path("podcast-id") podcastId: Int, @Header("Authorization") token: String
    ): Response<PodcastDetailedResponse>

    /**
     * Podcast. Add to saved(toggle mode)
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("podcasts/{podcast-id}/add-to-saved")
    suspend fun addToSavedPodcast(
        @Path("podcast-id") podcastId: Int, @Header("Authorization") token: String
    ): Response<Unit>

    /**
     * Episode. Get detailed
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("episodes/{episode-id}")
    suspend fun getEpisode(
        @Path("episode-id") episodeId: Int, @Header("Authorization") token: String
    ): Response<EpisodeDetailedResponse>

    /**
     * Episode. Like(toggle mode)
     * */
    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("episodes/{episode-id}/like")
    suspend fun likeEpisode(
        @Path("episode-id") episodeId: Int, @Header("Authorization") token: String
    ): Response<Unit>


    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("episodes/search")
    suspend fun searchEpisode(
        @QueryMap options: Map<String, String> , @Header("Authorization") token: String
    ): Response<SearchEpisodesData>


    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("guests")
    suspend fun getGuests(
        @Header("Authorization") token: String
    ): Response<GuestResponseData>


    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<CategoryResponseData>


    @Headers(
        "${HeaderName.Accept}: ${HeaderValue.ApplicationJson}",
        "${HeaderName.ContentType}: ${HeaderValue.ApplicationJson}"
    )
    @GET("topics")
    suspend fun getTopics(
        @Header("Authorization") token: String
    ): Response<TopicResponseData>
}
