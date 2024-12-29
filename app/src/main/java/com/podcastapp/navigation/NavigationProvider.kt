package com.podcastapp.navigation

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.MainApi
import com.podcastapp.podcast_details.ui.navigation.PodcastApi
import com.podcastapp.episode.details.ui.EpisodeApi
import com.podcastapp.profile.ui.navigation.ProfileApi
import com.podcastapp.ui.navigation.PlayerApi

data class NavigationProvider(
    val mainApi: MainApi,
    val authApi: AuthApi,
    val profileApi: ProfileApi,
    val podcastApi: PodcastApi,
    val episodeApi: EpisodeApi,
    val playerApi: PlayerApi
)