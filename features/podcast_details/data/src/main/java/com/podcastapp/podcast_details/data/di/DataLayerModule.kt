package com.podcastapp.podcast_details.data.di

import com.core.network.dataproviders.PodcastDataProviders
import com.podcastapp.podcast_details.data.repo.PodcastRepoImpl
import com.podcastapp.podcast_details.domain.repo.PodcastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {
    @Provides
    fun providePodcastRepo(podcastDataProviders: PodcastDataProviders): PodcastRepository {
        return PodcastRepoImpl(podcastDataProviders)
    }
}