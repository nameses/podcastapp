package com.podcastapp.episode.details.data.di

import com.core.network.dataproviders.EpisodeDataProviders
import com.podcastapp.episode.details.data.repo.EpisodeRepoImpl
import com.podcastapp.episode.details.domain.repo.EpisodeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {
    @Provides
    fun provideEpisodeRepo(episodeDataProviders: EpisodeDataProviders): EpisodeRepository {
        return EpisodeRepoImpl(episodeDataProviders)
    }
}