package com.podcastapp.podcast_details.domain.use_cases

import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import com.podcastapp.podcast_details.domain.model.Podcast
import com.podcastapp.podcast_details.domain.repo.PodcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetPodcastUseCase @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val commonPodcastRepository: CommonPodcastRepository
) {
    operator fun invoke(id: Int) = flow<UiEvent<Podcast>> {
        emit(UiEvent.Loading())

        when (val response = podcastRepository.getPodcast(id)) {
            is RepoEvent.Success -> if (response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun toggleSaveStatus(id: Int) {
        commonPodcastRepository.addToSaved(id)
    }
}