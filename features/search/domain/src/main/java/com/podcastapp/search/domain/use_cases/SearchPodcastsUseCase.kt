package com.podcastapp.search.domain.use_cases

import com.core.common.model.RepoEvent
import com.podcastapp.search.domain.models.SearchParams
import com.podcastapp.search.domain.repo.SearchRepository
import com.core.common.model.UiEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject
import com.podcastapp.search.domain.models.EpisodeList
import kotlinx.coroutines.Dispatchers


class SearchPodcastsUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    operator fun invoke(searchParams: SearchParams) = flow<UiEvent<EpisodeList>> {
        emit(UiEvent.Loading())
        when (val response = searchRepository.searchEpisodes(searchParams)) {
            is RepoEvent.Success -> if (response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }

    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}