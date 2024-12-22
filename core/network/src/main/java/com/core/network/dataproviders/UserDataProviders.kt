package com.core.network.dataproviders

import com.core.network.ApiService
import com.core.network.model.user.LoginRequest
import com.core.network.model.user.PurchasePremiumRequest
import com.core.network.model.user.RegisterRequest
import com.core.network.model.user.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserDataProviders @Inject constructor(private val apiService: ApiService) {
    suspend fun getUser() = apiService.getUser()
    suspend fun editUser(username: RequestBody, image: MultipartBody.Part) = apiService.updateUser(username = username, image = image)
    suspend fun purchasePremium(purchasePremiumRequest: PurchasePremiumRequest) = apiService.purchasePremium(purchasePremiumRequest)
}