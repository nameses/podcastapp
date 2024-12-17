package com.core.network.model

data class PodcastListResponse(
    val page: Int,
    val results: List<PodcastDTO>,
    val totalPages: Int,
    val totalResults: Int
)
