package com.podcastapp.search.domain.repo

import com.podcastapp.search.domain.models.SearchParams
import com.core.common.model.RepoEvent
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.EpisodeList


interface SearchRepository {
    suspend fun searchEpisodes(searchParams: SearchParams): RepoEvent<EpisodeList>
    suspend fun getAvailableFilters():RepoEvent<AvailableFilters>
}