package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.dataproviders.EpisodeDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.core.network.model.episodes.EpisodeDetailedResponse
import com.core.network.model.episodes.EpisodeFullDTO
import com.google.gson.Gson
import javax.inject.Inject

class CommonEpisodeRepoImpl @Inject constructor(private val episodeDataProvider: EpisodeDataProviders) :
    CommonEpisodeRepository {
    override suspend fun getEpisode(episodeId: Int): RepoEvent<EpisodeDetailedResponse> {
        val response = episodeDataProvider.getEpisode(episodeId)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }

    override suspend fun likeEpisode(episodeId: Int): RepoEvent<Unit> {
        val response = episodeDataProvider.likeEpisode(episodeId)

        return if (response.isSuccessful) {
            RepoEvent.Success(Unit)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}