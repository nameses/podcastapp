package com.podcastapp.profile.data.mapper

import com.core.network.model.user.UserDTO
import com.core.network.model.user.UserFullDTO
import com.podcastapp.profile.domain.model.Episode
import com.podcastapp.profile.domain.model.Podcast
import com.podcastapp.profile.domain.model.User
import com.podcastapp.profile.domain.model.UserFull


fun UserFullDTO.toUserFull(): UserFull = UserFull(email = email,
    username = username,
    imageUrl = image_url,
    premium = premium,
    likedEpisodes = liked_episodes.map {
        Episode(
            it.id, it.title, it.description, it.image_url, it.file_path, it.duration
        )
    },
    savedPodcasts = saved_podcasts.map {
        Podcast(
            it.id, it.title, /*it.author.name*/"dummy author", it.image_url, it.is_saved
        )
    })

fun UserDTO.toUser(): User = User(
    email = email, username = username, image_url = image_url, premium = premium
)
