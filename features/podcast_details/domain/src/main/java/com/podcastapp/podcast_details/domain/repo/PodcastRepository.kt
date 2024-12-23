package com.podcastapp.podcast_details.domain.repo

import com.core.common.model.RepoEvent
import com.podcastapp.podcast_details.domain.model.Podcast

interface PodcastRepository {
    suspend fun getPodcast(podcastId: Int) : RepoEvent<Podcast>
}