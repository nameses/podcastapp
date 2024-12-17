package com.features.main.domain.di

import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.use_cases.GetPopularPodcastListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DomainLayerModule {
    @Provides
    fun provideGetPopularPodcastListUseCase(podcastRepository: PodcastRepository): GetPopularPodcastListUseCase {
        return GetPopularPodcastListUseCase(podcastRepository)
    }
}