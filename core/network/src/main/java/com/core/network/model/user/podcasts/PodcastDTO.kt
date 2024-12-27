package com.core.network.model.user.podcasts

data class PodcastDTO(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String?,
    val language: String,
    val featured: Boolean,
    val author: Author,
)

data class Author(
    val name: String
)