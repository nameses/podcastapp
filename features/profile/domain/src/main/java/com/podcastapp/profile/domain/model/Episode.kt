package com.podcastapp.profile.domain.model

data class Episode(
    var id: Int,
    var title: String,
    var description: String,
    var imageUrl: String?,
    var filePath: String,
    var duration: Int,
    var author: String
)