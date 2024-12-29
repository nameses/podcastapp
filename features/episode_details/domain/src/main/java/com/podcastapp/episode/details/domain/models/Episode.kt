package com.podcastapp.episode.details.domain.models

data class Episode(
    var id: Int,
    var title: String,
    var description: String,
    var imageUrl: String?,
    var guests: List<Guest>,
    var author: String,
    var isLiked: Boolean,
    val duration: Int,
    val likesAmount: Int,
    val language: String,
)