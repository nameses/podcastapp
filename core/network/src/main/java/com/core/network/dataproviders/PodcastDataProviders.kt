package com.core.network.dataproviders

import com.core.network.ApiService
import javax.inject.Inject

class PodcastDataProviders @Inject constructor(private val apiService: ApiService) {

    suspend fun getPodcastListFeatured() = apiService.GetPodcastListFeatured()

    suspend fun getPodcastListPopular() = apiService.GetPodcastListPopular()
}