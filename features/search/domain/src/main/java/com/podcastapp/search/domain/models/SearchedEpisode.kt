package com.podcastapp.search.domain.models

data class SearchedEpisode(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val duration: Int,
)
