package com.features.main.data.mapper

import com.core.network.model.podcasts.PodcastListResponse
import com.features.main.domain.model.PodcastDTO
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
        PodcastDTO(
            id = podcastItem.id,
            title = podcastItem.title,
            description = podcastItem.description,
            image_url = podcastItem.imageURL
        )
    }

    return PodcastList(
        pagination = mappedPagination,
        items = mappedItems
    )
}