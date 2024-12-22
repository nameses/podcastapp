package com.core.network.model.podcasts


data class PodcastListItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageURL: String,
    val language: String,
    val featured: Boolean
)
