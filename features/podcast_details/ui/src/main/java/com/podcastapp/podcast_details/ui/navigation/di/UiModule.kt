package com.podcastapp.podcast_details.ui.navigation.di

import com.podcastapp.podcast_details.ui.navigation.PodcastApi
import com.podcastapp.podcast_details.ui.navigation.PodcastApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun providePodcastApi(): PodcastApi {
        return PodcastApiImpl()
    }
}