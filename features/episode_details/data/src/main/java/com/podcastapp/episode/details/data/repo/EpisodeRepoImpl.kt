package com.podcastapp.episode.details.data.repo

import com.core.network.dataproviders.EpisodeDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.podcastapp.episode.details.domain.models.Episode
import com.core.common.model.RepoEvent
import com.google.gson.Gson
import com.podcastapp.episode.details.data.mapper.toDomainEpisode
import com.podcastapp.episode.details.domain.repo.EpisodeRepository
import javax.inject.Inject

class EpisodeRepoImpl @Inject constructor(private val episodeDataProvider: EpisodeDataProviders) : EpisodeRepository {
    override suspend fun getEpisode(episodeId: Int): RepoEvent<Episode> {
        val response = episodeDataProvider.getEpisode(episodeId)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!.toDomainEpisode())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}