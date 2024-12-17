package com.features.main.data.mapper

import com.core.network.model.PodcastListResponse
import com.features.main.domain.model.Podcast

fun PodcastListResponse.toDomainPodcastList(): List<Podcast> {
    return this.results.map {
        Podcast(it.title, it.author, it.imagePath)
    }
}