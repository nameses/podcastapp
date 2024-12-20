package com.features.main.domain.repo

import com.features.main.domain.model.Podcast

interface PodcastRepository {
    suspend fun getPodcastList(podcastType: PodcastType) : List<Podcast>
}

enum class PodcastType { Featured, Popular }