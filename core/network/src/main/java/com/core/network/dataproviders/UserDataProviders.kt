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
            tokenManager.getFormattedTokenOrEmpty()
        )

    suspend fun editUser(params: Map<String, RequestBody>, image: MultipartBody.Part) =
        apiService.updateUser(
            params = params,
            image = image,
            tokenManager.getFormattedTokenOrEmpty()
        )

    suspend fun purchasePremium(purchasePremiumRequest: PurchasePremiumRequest) =
        apiService.purchasePremium(
            purchasePremiumRequest,
            tokenManager.getFormattedTokenOrEmpty()
        )
}