package com.features.main.domain.use_cases

import com.core.common.UiEvent
import com.features.main.domain.model.Podcast
import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.repo.PodcastType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPopularPodcastListUseCase @Inject constructor(private val podcastRepository: PodcastRepository) {
    operator fun invoke() = flow<UiEvent<List<Podcast>>> {
        emit(UiEvent.Loading())
        emit(UiEvent.Success(podcastRepository.getPodcastList(PodcastType.Popular)))
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}