package com.podcastapp.search.data.repo

import javax.inject.Inject
import com.core.network.dataproviders.SearchDataProvider
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.EpisodeList
import com.podcastapp.search.domain.models.SearchParams
import com.podcastapp.search.domain.repo.SearchRepository
import com.core.common.model.RepoEvent
import com.core.network.model.common.ValidationErrorResponse
import com.google.gson.Gson
import com.podcastapp.search.data.mapper.toAvailableFilters
import com.podcastapp.search.data.mapper.toEpisodeList
import com.podcastapp.search.data.mapper.toQueryMap

class SearchRepositoryImpl @Inject constructor(private val searchDataProvider: SearchDataProvider) : SearchRepository {
    override suspend fun searchEpisodes(searchParams: SearchParams): RepoEvent<EpisodeList> {
        val response = searchDataProvider.getEpisodeSearch(searchParams.toQueryMap())

        return if (response.isSuccessful && response.body() != null) {
            RepoEvent.Success(response.body()!!.data.toEpisodeList())
        } else {
            val errorResponse = response.errorBody()?.let {
                Gson().fromJson(it.string(), ValidationErrorResponse::class.java)
            }
            RepoEvent.Error(errorResponse?.message ?: "Unknown error", errorResponse?.errors)
        }
    }

    override suspend fun getAvailableFilters(): RepoEvent<AvailableFilters> {
        val topicsResponse = searchDataProvider.getTopics()
        val categoryResponse = searchDataProvider.getCategories()
        val guestResponse = searchDataProvider.getGuests()

        return if (topicsResponse.isSuccessful && topicsResponse.body() != null &&
            categoryResponse.isSuccessful && categoryResponse.body() != null &&
            guestResponse.isSuccessful && guestResponse.body() != null
                ) {
            RepoEvent.Success(toAvailableFilters(
                topicsResponse.body()!!.data,
                guestResponse.body()!!.data,
                categoryResponse.body()!!.data
                ))
        } else {
            RepoEvent.Error("Error when getting available filters")
        }
    }
}