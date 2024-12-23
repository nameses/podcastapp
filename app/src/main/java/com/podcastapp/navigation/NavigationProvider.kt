package com.podcastapp.navigation

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.PodcastApi
import com.podcastapp.profile.ui.navigation.ProfileApi

data class NavigationProvider(
    val podcastApi: PodcastApi,
    val authApi: AuthApi,
    val profileApi: ProfileApi
)