package com.features.main.ui.navigation.di

import com.features.main.ui.navigation.MainApi
import com.features.main.ui.navigation.MainApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun provideMainApi(): MainApi {
        return MainApiImpl()
    }
}