package com.core.network.model.user

import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.podcasts.PodcastDTO

data class UserFullDTO(
    val email: String,
    val username: String,
    val image_url: String?,
    val premium: Boolean,
    val liked_episodes: List<EpisodeDTO>,
    val saved_podcasts: List<PodcastDTO>
)