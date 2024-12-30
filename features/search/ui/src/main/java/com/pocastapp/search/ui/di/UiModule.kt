package com.pocastapp.search.ui.di

import com.pocastapp.search.ui.SearchApi
import com.pocastapp.search.ui.SearchApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun provideSearchApi(): SearchApi {
        return SearchApiImpl()
    }
}