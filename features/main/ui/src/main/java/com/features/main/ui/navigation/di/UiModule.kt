package com.features.main.ui.navigation.di

import com.features.main.ui.navigation.PodcastApi
import com.features.main.ui.navigation.PodcastApiImpl
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