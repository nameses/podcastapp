package com.podcastapp.profile.domain.model


data class UserFull(
    val email: String,
    val username: String,
    val imageUrl: String?,
    val premium: Boolean,
    val likedEpisodes: List<Episode>,
    val savedPodcasts: List<Podcast>
)