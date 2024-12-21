package com.core.network.model.podcasts

data class PodcastListResponse(
    val page: Int,
    val results: List<PodcastDTO>,
    val totalPages: Int,
    val totalResults: Int
)
