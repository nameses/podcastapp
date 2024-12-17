package com.features.auth.data.di

import com.core.network.dataproviders.AuthDataProviders
import com.features.auth.data.repo.AuthRepoImpl
import com.features.auth.domain.repo.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {
    @Provides
    fun providePodcastRepo(podcastDataProviders: AuthDataProviders): AuthRepository {
        return AuthRepoImpl(podcastDataProviders)
    }
}