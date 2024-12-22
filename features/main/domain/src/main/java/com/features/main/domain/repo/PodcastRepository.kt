package com.features.main.domain.repo

import com.features.main.domain.model.PodcastList

interface PodcastRepository {
    suspend fun getPodcastList(podcastType: PodcastType) : PodcastList
}

enum class PodcastType { Featured, Popular }