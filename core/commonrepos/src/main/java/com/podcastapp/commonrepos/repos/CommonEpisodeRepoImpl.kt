package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.core.network.model.episodes.EpisodeFullDTO
import com.google.gson.Gson
import javax.inject.Inject

class CommonEpisodeRepoImpl @Inject constructor(private val episodeDataProvider: EpisodeDataProviders) :
    CommonEpisodeRepository {
    override fun getEpisode(episodeId: Int): RepoEvent<EpisodeFullDTO> {
        val response = episodeDataProvider.getEpisode(episodeId)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(Unit)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}