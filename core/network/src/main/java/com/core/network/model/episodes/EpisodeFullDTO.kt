package com.core.network.model.episodes

data class EpisodeFullDTO (
    var id: Int,
    val title: String,
    var description: String,
    var image_url: String,
    var duration: Int,
    var episode_number: Int,
    var file_path: String,
)