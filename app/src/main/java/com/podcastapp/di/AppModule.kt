package com.podcastapp.di

import com.features.main.ui.navigation.PodcastApi
import com.podcastapp.navigation.NavigationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideNavigationProvider(podcastApi: PodcastApi):NavigationProvider{
        return NavigationProvider(podcastApi)
    }

}