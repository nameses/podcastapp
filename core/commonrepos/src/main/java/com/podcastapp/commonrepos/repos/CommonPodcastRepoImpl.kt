package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.core.network.model.podcasts.PodcastDTO
import com.google.gson.Gson
import javax.inject.Inject

class CommonPodcastRepoImpl
@Inject constructor(private val podcastDataProvider: PodcastDataProviders) :
    CommonPodcastRepository {
    override suspend fun addToSaved(podcastId: Int): RepoEvent<Unit> {
        val response = podcastDataProvider.addToSavedPodcast(podcastId)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(Unit)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }

    override suspend fun getPodcast(id: Int): RepoEvent<PodcastDTO> {
        val response = podcastDataProvider.getPodcast(id)

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!)
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}