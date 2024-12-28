package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeFullDTO

interface CommonEpisodeRepository {
    fun getEpisode(episodeId:Int): RepoEvent<EpisodeFullDTO>
}