package com.podcastapp.profile.data.di

import com.core.network.dataproviders.AuthDataProviders
import com.core.network.dataproviders.UserDataProviders
import com.podcastapp.profile.data.repo.UserRepoImpl
import com.podcastapp.profile.domain.repo.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {
    @Singleton
    @Provides
    fun provideUserRepo(userDataProviders: UserDataProviders): UserRepository {
        return UserRepoImpl(userDataProviders)
    }
}