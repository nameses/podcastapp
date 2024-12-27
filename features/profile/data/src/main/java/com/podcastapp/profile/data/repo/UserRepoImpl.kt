package com.podcastapp.profile.data.repo

import android.util.Log
import com.core.common.model.RepoEvent
import com.core.network.dataproviders.UserDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.core.network.model.user.PurchasePremiumRequest
import com.google.gson.Gson
import com.podcastapp.profile.data.mapper.toUser
import com.podcastapp.profile.data.mapper.toUserFull
import com.podcastapp.profile.domain.model.User
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.repo.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepoImpl
@Inject constructor(private val userDataProvider: UserDataProviders) : UserRepository {
    override suspend fun getProfile(): RepoEvent<UserFull> {
        val response = userDataProvider.getUser()

        return if (response.isSuccessful && response.body() != null) {

            RepoEvent.Success(response.body()!!.data.toUserFull())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }

    override suspend fun editProfile(
        params: Map<String, RequestBody>,
        image_url: MultipartBody.Part
    ): RepoEvent<User> {
        val response = userDataProvider.editUser(params, image_url)
        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!.data.toUser())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }

    override suspend fun purchasePremium(
        cvv: String,
        cardNumber: String,
        expirationDate: String,
        cardHolder: String
    ): RepoEvent<Boolean> {
        val response = userDataProvider.purchasePremium(
            PurchasePremiumRequest(
                cvv,
                cardNumber,
                expirationDate,
                cardHolder
            )
        )
        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(true)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}