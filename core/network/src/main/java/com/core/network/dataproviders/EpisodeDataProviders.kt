package com.core.network.dataproviders

import com.core.common.services.TokenManager
import com.core.network.ApiService
import javax.inject.Inject

class EpisodeDataProviders @Inject constructor(
    private val apiService: ApiService, private val tokenManager: TokenManager
) {
    suspend fun getEpisode(episodeId: Int) = apiService.getEpisode(
        episodeId, tokenManager.getFormattedTokenOrEmpty()
    )
    suspend fun likeEpisode(episodeId: Int) = apiService.likeEpisode(
        episodeId, tokenManager.getFormattedTokenOrEmpty()
    )
}