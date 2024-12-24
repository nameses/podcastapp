package com.core.network.model.episodes


data class EpisodeDTO(
    var id: Int,
    val title: String,
    var description: String,
    var duration: Int,
    var episode_number: Int,
    var file_path: String,
    var podcast_id: Int,
    var category_id: Int
)