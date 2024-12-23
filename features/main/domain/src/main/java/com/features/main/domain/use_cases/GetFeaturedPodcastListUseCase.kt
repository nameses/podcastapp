package com.features.main.domain.use_cases

import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.features.main.domain.model.PodcastList
import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.repo.PodcastType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFeaturedPodcastListUseCase @Inject constructor(private val podcastRepository: PodcastRepository) {
    operator fun invoke(page: Int) = flow<UiEvent<PodcastList>> {
        emit(UiEvent.Loading())

        when (val response = podcastRepository.getPodcastList(PodcastType.Featured, page)) {
            is RepoEvent.Success -> if (response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}