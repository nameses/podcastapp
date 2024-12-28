package com.podcastapp.di

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.MainApi
import com.podcastapp.navigation.NavigationProvider
import com.podcastapp.podcast_details.ui.navigation.PodcastApi
import com.podcastapp.profile.ui.navigation.ProfileApi
import com.podcastapp.ui.navigation.PlayerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideNavigationProvider(
        mainApi: MainApi,
        authApi: AuthApi,
        profileApi: ProfileApi,
        podcastApi: PodcastApi,
        playerApi: PlayerApi
    ): NavigationProvider {
        return NavigationProvider(mainApi, authApi, profileApi, podcastApi, playerApi)
    }
}