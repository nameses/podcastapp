package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import javax.inject.Inject

class PodcastDataProviders @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun getPodcastListFeatured(page: Int) = apiService.GetPodcastListFeatured(
        page,
        tokenManager.getFormattedToken().takeIf { tokenManager.containsJwtToken() } ?: "")

    suspend fun getPodcastListPopular(page: Int) = apiService.GetPodcastListPopular(
        page,
        tokenManager.getFormattedToken().takeIf { tokenManager.containsJwtToken() } ?: "")

}