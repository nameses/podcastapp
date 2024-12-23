package com.podcastapp.di

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.PodcastApi
import com.podcastapp.navigation.NavigationProvider
import com.podcastapp.profile.ui.navigation.ProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideNavigationProvider(
        podcastApi: PodcastApi,
        authApi: AuthApi,
        profileApi: ProfileApi
    ): NavigationProvider {
        return NavigationProvider(podcastApi, authApi, profileApi)
    }
}