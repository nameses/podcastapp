package com.podcastapp.profile.domain.repo

import com.core.common.model.RepoEvent
import com.podcastapp.profile.domain.model.User
import com.podcastapp.profile.domain.model.UserFull
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun getProfile(): RepoEvent<UserFull>
    suspend fun editProfile(params: Map<String, RequestBody>, image_url: MultipartBody.Part): RepoEvent<User>
    suspend fun purchasePremium(
        cvv: String,
        cardNumber: String,
        expirationDate: String,
        cardHolder: String
    ): RepoEvent<Boolean>
}