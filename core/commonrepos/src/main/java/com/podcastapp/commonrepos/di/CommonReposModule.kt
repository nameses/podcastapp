package com.podcastapp.commonrepos.di

import com.core.network.dataproviders.EpisodeDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import com.podcastapp.commonrepos.repos.CommonEpisodeRepoImpl
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.commonrepos.repos.CommonPodcastRepoImpl
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonReposModule {

    @Provides
    @Singleton
    fun provideCommonPodcastRepository(podcastDataProvider: PodcastDataProviders): CommonPodcastRepository {
        return CommonPodcastRepoImpl(podcastDataProvider)
    }
    @Provides
    @Singleton
    fun provideCommonEpisodeRepository(episodeDataProvider: EpisodeDataProviders): CommonEpisodeRepository {
        return CommonEpisodeRepoImpl(episodeDataProvider)
    }
}