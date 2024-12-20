package com.features.auth.data.di

import com.core.network.dataproviders.AuthDataProviders
import com.features.auth.data.repo.AuthRepoImpl
import com.features.auth.domain.repo.AuthRepository
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
    fun provideAuthRepo(authDataProviders: AuthDataProviders): AuthRepository {
        return AuthRepoImpl(authDataProviders)
    }
    //@Singleton
    //@Binds
    //abstract fun bindAuthRepository(impl: AuthRepoImpl): AuthRepository
}