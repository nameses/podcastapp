package com.podcastapp.episode.details.ui.di

import com.podcastapp.episode.details.ui.EpisodeApi
import com.podcastapp.episode.details.ui.EpisodeApiImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun provideEpisodeApi(): EpisodeApi {
        return EpisodeApiImpl()
    }
}