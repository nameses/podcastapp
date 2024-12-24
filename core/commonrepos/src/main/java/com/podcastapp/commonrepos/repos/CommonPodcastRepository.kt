package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.core.network.model.podcasts.PodcastDTO

interface CommonPodcastRepository {
    suspend fun addToSaved(podcastId: Int) : RepoEvent<Unit>
    suspend fun getPodcast(id: Int) : RepoEvent<PodcastDTO>
}