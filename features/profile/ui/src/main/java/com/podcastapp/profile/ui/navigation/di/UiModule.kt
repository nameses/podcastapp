package com.podcastapp.profile.ui.navigation.di

import com.podcastapp.profile.ui.navigation.ProfileApi
import com.podcastapp.profile.ui.navigation.ProfileApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun providePodcastApi(): ProfileApi {
        return ProfileApiImpl()
    }
}