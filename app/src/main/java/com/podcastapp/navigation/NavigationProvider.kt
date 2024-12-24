package com.podcastapp.navigation

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.MainApi
import com.podcastapp.podcast_details.ui.navigation.PodcastApi
import com.podcastapp.profile.ui.navigation.ProfileApi

data class NavigationProvider(
    val mainApi: MainApi,
    val authApi: AuthApi,
    val profileApi: ProfileApi,
    val podcastApi: PodcastApi
)