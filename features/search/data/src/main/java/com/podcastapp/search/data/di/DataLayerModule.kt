package com.podcastapp.search.data.di

import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.dataproviders.SearchDataProvider
import com.podcastapp.search.data.repo.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {
    @Provides
    fun provideSearchRepo(searchDataProvider: SearchDataProvider): SearchRepositoryImpl {
        return SearchRepositoryImpl(searchDataProvider)
    }
}