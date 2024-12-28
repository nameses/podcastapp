package com.podcastapp.domain.use_cases

import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.core.network.model.episodes.EpisodeFullDTO
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetEpisodeUseCase @Inject constructor(private val commonEpisodeRepository: CommonEpisodeRepository) {
    operator fun invoke(id:Int) = flow<UiEvent<EpisodeFullDTO>> {
        emit(UiEvent.Loading())

        when (val response = commonEpisodeRepository.getEpisode(id)) {
            is RepoEvent.Success -> emit(UiEvent.Success(response.data!!.data))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message ?: "", response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}