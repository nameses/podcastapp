package com.podcastapp.search.domain.di

import com.podcastapp.search.domain.repo.SearchRepository
import com.podcastapp.search.domain.use_cases.GetAvailableFiltersUseCase
import com.podcastapp.search.domain.use_cases.SearchPodcastsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {
    @Provides
    fun provideGetAvailableFiltersUseCase(searchRepository: SearchRepository): GetAvailableFiltersUseCase {
        return GetAvailableFiltersUseCase(searchRepository)
    }

    @Provides
    fun provideSearchPodcastsUseCase(searchRepository: SearchRepository): SearchPodcastsUseCase {
        return SearchPodcastsUseCase(searchRepository)
    }
}