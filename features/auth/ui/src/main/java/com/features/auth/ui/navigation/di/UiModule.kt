package com.features.auth.ui.navigation.di

import com.features.auth.ui.navigation.AuthApi
import com.features.auth.ui.navigation.AuthApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun providePodcastApi(): AuthApi {
        return AuthApiImpl()
    }
}