package com.podcastapp.episode.details.domain.di

import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.episode.details.domain.use_cases.GetEpisodeUseCase
import com.podcastapp.episode.details.domain.repo.EpisodeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {
    @Provides
    fun provideGetEpisodeUseCase(
        episodeRepository: EpisodeRepository,
        commonEpisodeRepository: CommonEpisodeRepository
    ): GetEpisodeUseCase {
        return GetEpisodeUseCase(episodeRepository, commonEpisodeRepository)
    }
}