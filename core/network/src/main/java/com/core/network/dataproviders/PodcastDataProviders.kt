package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import javax.inject.Inject

class PodcastDataProviders @Inject constructor(
    private val apiService: ApiService, private val tokenManager: TokenManager
) {

    suspend fun getPodcastListFeatured(page: Int) = apiService.GetPodcastListFeatured(
        page, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getPodcastListPopular(page: Int) = apiService.GetPodcastListPopular(
        page, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun addToSavedPodcast(podcastId: Int) = apiService.AddToSavedPodcast(
        podcastId, tokenManager.getFormattedTokenOrEmpty()
    )
}