package com.podcastapp.podcast_details.domain.model

data class Podcast(
    var id: Int,
    var title: String,
    var description: String,
    var imageUrl: String?,
    var episodes: List<Episode>,
    var author: String,
    var isSaved: Boolean
)