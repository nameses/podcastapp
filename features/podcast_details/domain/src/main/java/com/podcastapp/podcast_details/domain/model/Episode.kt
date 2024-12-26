package com.podcastapp.podcast_details.domain.model

data class Episode(
    var id: Int,
    var title: String,
    var description: String,
    var imageUrl: String?,
    var filePath: String,
    var duration: Int
)