package com.podcastapp.domain.di

import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.domain.use_cases.GetEpisodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {
    @Provides
    fun provideGetEpisodeUseCase(commonEpisodeRepository: CommonEpisodeRepository): GetEpisodeUseCase {
        return GetEpisodeUseCase(commonEpisodeRepository)
    }
}