package com.core.network.di

import com.core.common.services.TokenManager
import com.core.network.ApiService
import com.core.network.dataproviders.AuthDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.interceptors.AuthTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder().baseUrl("http://localhost/api")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authTokenInterceptor: AuthTokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authTokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthTokenInterceptor(tokenManager: TokenManager): AuthTokenInterceptor {
        return AuthTokenInterceptor(tokenManager)
    }

    //api parts providing
    @Provides
    fun providePodcastDataProvider(apiService: ApiService): PodcastDataProviders {
        return PodcastDataProviders(apiService)
    }

    @Provides
    fun provideAuthDataProviders(apiService: ApiService): AuthDataProviders {
        return AuthDataProviders(apiService)
    }
}