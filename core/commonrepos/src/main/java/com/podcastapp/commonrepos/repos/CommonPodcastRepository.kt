package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent

interface CommonPodcastRepository {
    suspend fun addToSaved(podcastId: Int) : RepoEvent<Unit>
}