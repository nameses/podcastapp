package com.core.network.model.podcasts

data class SearchEpisodesData(
    val success: Boolean,
    val data: Data
)


data class Data(
    val pagination: Pagination,
    val items: List<SearchedEpisode>
)


data class Pagination(
    val total: Int,
    val per_page: Int,
    val current_page: Int,
    val last_page: Int
)


data class SearchedEpisode(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String,
    val duration: Int,
    val episode_number: Int,
    val file_path: String
)
