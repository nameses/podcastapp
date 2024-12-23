package com.features.main.data.repo

import com.core.common.model.RepoEvent
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.features.main.data.mapper.toDomainPodcastList
import com.features.main.domain.model.PodcastList
import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.repo.PodcastType
import com.google.gson.Gson
import javax.inject.Inject

class PodcastRepoImpl
@Inject constructor(private val podcastDataProvider: PodcastDataProviders) : PodcastRepository {
    override suspend fun getPodcastList(podcastType: PodcastType, page: Int): RepoEvent<PodcastList> {

        val response = when (podcastType){
            PodcastType.Featured -> podcastDataProvider.getPodcastListFeatured(page)
            PodcastType.Popular -> podcastDataProvider.getPodcastListPopular(page)
        }
        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!.toDomainPodcastList())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }
}