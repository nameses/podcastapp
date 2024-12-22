package com.podcastapp.profile.domain.model


data class UserFull(
    val email: String,
    val username: String,
    val image_url: String,
    val premium: Boolean,
    val liked_episodes: List<Episode>,
    val saved_podcasts: List<Podcast>
)