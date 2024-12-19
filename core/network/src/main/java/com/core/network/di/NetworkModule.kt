package com.core.network.di

import com.core.network.ApiService
import com.core.network.dataproviders.AuthDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl("http://localhost/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun providePodcastDataProvider(apiService: ApiService): PodcastDataProviders {
        return PodcastDataProviders(apiService)
    }

    @Provides
    fun provideAuthDataProviders(apiService: ApiService): AuthDataProviders {
        return AuthDataProviders(apiService)
    }
}