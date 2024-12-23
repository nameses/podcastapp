package com.features.main.domain.repo

import com.core.common.model.RepoEvent
import com.features.main.domain.model.PodcastList

interface PodcastRepository {
    suspend fun getPodcastList(podcastType: PodcastType, page: Int) : RepoEvent<PodcastList>
}

enum class PodcastType { Featured, Popular }