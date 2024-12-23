package com.podcastapp.podcast_details.domain.di

import com.podcastapp.podcast_details.domain.repo.PodcastRepository
import com.podcastapp.podcast_details.domain.use_cases.GetPodcastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {
    @Provides
    fun provideGetPopularPodcastListUseCase(podcastRepository: PodcastRepository): GetPodcastUseCase {
        return GetPodcastUseCase(podcastRepository)
    }
}