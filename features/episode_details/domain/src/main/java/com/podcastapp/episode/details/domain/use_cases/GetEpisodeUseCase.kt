package com.podcastapp.episode.details.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.core.common.model.UiEvent
import com.core.common.model.RepoEvent
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.episode.details.domain.models.Episode
import com.podcastapp.episode.details.domain.repo.EpisodeRepository


class GetEpisodeUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository,
    private val commonEpisodeRepository: CommonEpisodeRepository
) {
    operator fun invoke(id: Int) = flow<UiEvent<Episode>> {
        emit(UiEvent.Loading())

        when (val response = episodeRepository.getEpisode(id)) {
            is RepoEvent.Success -> if (response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun toggleLikeStatus(id: Int) {
        commonEpisodeRepository.likeEpisode(id)
    }
}