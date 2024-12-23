package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import com.core.network.model.user.PurchasePremiumRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserDataProviders @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getUser() =
        apiService.getUser(
            tokenManager.getFormattedToken().takeIf { tokenManager.containsJwtToken() } ?: "")

    suspend fun editUser(username: RequestBody, image: MultipartBody.Part) =
        apiService.updateUser(
            username = username,
            image = image,
            tokenManager.getFormattedToken().takeIf { tokenManager.containsJwtToken() } ?: "")

    suspend fun purchasePremium(purchasePremiumRequest: PurchasePremiumRequest) =
        apiService.purchasePremium(
            purchasePremiumRequest,
            tokenManager.getFormattedToken().takeIf { tokenManager.containsJwtToken() } ?: "")
}