package com.core.network.model.episodes

data class PodcastDTO(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String?,
    val language: String,
    val featured: Boolean,
    val episodes: List<EpisodeDTO>,
    val author: Author,
)

data class Author(
    val name: String
)