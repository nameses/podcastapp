package com.podcastapp.episode.details.domain.repo

import com.core.common.model.RepoEvent
import com.podcastapp.episode.details.domain.models.Episode

interface EpisodeRepository {
    suspend fun getEpisode(episodeId: Int) : RepoEvent<Episode>
}