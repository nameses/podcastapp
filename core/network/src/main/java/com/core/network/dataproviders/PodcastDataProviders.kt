package com.core.network.dataproviders

import com.core.network.ApiService
import javax.inject.Inject

class PodcastDataProviders @Inject constructor(private val apiService: ApiService) {
    suspend fun getPodcastListByQuery(query: String) = apiService.GetPodcastListByQuery(query)

    suspend fun getPodcastListFeatured() = apiService.GetPodcastListFeatured()

    suspend fun getPodcastListPopular() = apiService.GetPodcastListPopular()
}