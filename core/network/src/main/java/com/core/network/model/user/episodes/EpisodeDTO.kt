package com.core.network.model.user.episodes

import com.core.network.model.user.podcasts.PodcastDTO


data class EpisodeDTO(
    var id: Int,
    val title: String,
    var description: String,
    var image_url: String,
    var duration: Int,
    var episode_number: Int,
    var file_path: String,
    var podcast: PodcastDTO
)