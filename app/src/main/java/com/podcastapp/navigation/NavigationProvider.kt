package com.podcastapp.navigation

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.PodcastApi

data class NavigationProvider(
    val podcastApi: PodcastApi,
    val authApi: AuthApi
)