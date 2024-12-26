package com.features.main.data.mapper

import com.core.network.model.podcasts.PodcastListResponse
import com.features.main.domain.model.Podcast
import com.features.main.domain.model.PodcastList
import com.features.main.domain.model.PodcastPagination

fun PodcastListResponse.toDomainPodcastList(): PodcastList {
    val mappedPagination = PodcastPagination(
        total = this.data.pagination.total,
        perPage = this.data.pagination.per_page,
        currentPage = this.data.pagination.current_page,
        lastPage = this.data.pagination.last_page
    )

    val mappedItems = this.data.items.map { podcastItem ->
        Podcast(
            id = podcastItem.id,
            title = podcastItem.title,
            author = "dummy author",//podcastItem.author.name,
            imageUrl = podcastItem.image_url,
            isSaved = podcastItem.is_saved
        )
    }

    return PodcastList(
        pagination = mappedPagination,
        items = mappedItems
    )
}
