package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeDetailedResponse
import com.core.network.model.episodes.EpisodeFullDTO

interface CommonEpisodeRepository {
    suspend fun getEpisode(episodeId:Int): RepoEvent<EpisodeDetailedResponse>
    suspend fun likeEpisode(episodeId:Int): RepoEvent<Unit>
}