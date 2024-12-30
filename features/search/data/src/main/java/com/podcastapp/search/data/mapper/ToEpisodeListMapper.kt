package com.podcastapp.search.data.mapper

import com.podcastapp.search.domain.models.EpisodeList
import com.core.network.model.podcasts.Data
import com.podcastapp.search.domain.models.EpisodePagination
import com.podcastapp.search.domain.models.SearchedEpisode

fun Data.toEpisodeList(): EpisodeList {
    return EpisodeList(
        pagination = EpisodePagination(
            total = pagination.total,
            perPage = pagination.per_page,
            currentPage = pagination.current_page,
            lastPage = pagination.last_page
        ),
        items = items.map { episode ->
            SearchedEpisode(
                id = episode.id,
                title = episode.title,
                description = episode.description,
                image = episode.image_url,
                duration = episode.duration
            )
        }
    )
}