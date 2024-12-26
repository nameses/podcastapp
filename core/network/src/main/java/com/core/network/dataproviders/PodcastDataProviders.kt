package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import javax.inject.Inject

class PodcastDataProviders @Inject constructor(
    private val apiService: ApiService, private val tokenManager: TokenManager
) {

    suspend fun getPodcastListFeatured(page: Int) = apiService.getPodcastListFeatured(
        page, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getPodcastListPopular(page: Int) = apiService.getPodcastListPopular(
        page, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getPodcastListNew(page: Int) = apiService.getPodcastListNew(
        page, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun getPodcastFull(id: Int) = apiService.getPodcastFull(
        id, tokenManager.getFormattedTokenOrEmpty()
    )

    suspend fun addToSavedPodcast(podcastId: Int) = apiService.addToSavedPodcast(
        podcastId, tokenManager.getFormattedTokenOrEmpty()
    )
}