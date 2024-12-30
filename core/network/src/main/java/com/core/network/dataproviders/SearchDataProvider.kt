package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import javax.inject.Inject

class SearchDataProvider @Inject constructor(
    private val apiService: ApiService, private val tokenManager: TokenManager
) {
    suspend fun getEpisodeSearch(params: Map<String, String>) = apiService.searchEpisode(
        params, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getTopics() = apiService.getTopics(
        tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getCategories() = apiService.getCategories(
        tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getGuests() = apiService.getGuests(
        tokenManager.getFormattedTokenOrEmpty()
    )
}