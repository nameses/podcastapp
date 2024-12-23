package com.podcastapp.podcast_details.data.repo

import com.core.common.model.RepoEvent
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.features.main.data.mapper.toDomainPodcastList
import com.google.gson.Gson
import com.podcastapp.podcast_details.data.mapper.toDomainPodcast
import com.podcastapp.podcast_details.domain.model.Podcast
import com.podcastapp.podcast_details.domain.repo.PodcastRepository
import javax.inject.Inject

class PodcastRepoImpl
@Inject constructor(private val podcastDataProvider: PodcastDataProviders) : PodcastRepository {
    override suspend fun getPodcast(podcastId: Int): RepoEvent<Podcast> {
        val response = podcastDataProvider.getPodcastFull(podcastId)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!.toDomainPodcast())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}
