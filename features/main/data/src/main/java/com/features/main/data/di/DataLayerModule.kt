package com.features.main.data.di

import com.core.network.dataproviders.PodcastDataProviders
import com.features.main.data.repo.PodcastRepoImpl
import com.features.main.domain.repo.PodcastRepository
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

    //@Binds
    //abstract fun bindAuthRepository(impl: PodcastRepoImpl): PodcastRepository
}