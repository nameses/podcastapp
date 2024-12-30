package com.podcastapp.di

import com.features.auth.ui.navigation.AuthApi
import com.features.main.ui.navigation.MainApi
import com.pocastapp.search.ui.SearchApi
import com.podcastapp.episode.details.ui.EpisodeApi
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
        episodeApi: EpisodeApi,
        playerApi: PlayerApi,
        searchApi: SearchApi
    ): NavigationProvider {
        return NavigationProvider(mainApi, authApi, profileApi, podcastApi, episodeApi, playerApi, searchApi)
    }
}