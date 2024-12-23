package com.core.network.di

import com.core.common.services.TokenManager
import com.core.network.ApiService
import com.core.network.converterfactories.MultipartConverterFactory
import com.core.network.dataproviders.AuthDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.dataproviders.UserDataProviders
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        val gson = GsonBuilder()
            .setLenient()
            .create();

        return Retrofit.Builder().baseUrl("http://192.168.0.102/api/")
            .addConverterFactory(MultipartConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    //api parts providing
    @Provides
    fun providePodcastDataProvider(
        apiService: ApiService,
        tokenManager: TokenManager
    ): PodcastDataProviders {
        return PodcastDataProviders(apiService, tokenManager)
    }

    @Provides
    fun provideAuthDataProviders(apiService: ApiService): AuthDataProviders {
        return AuthDataProviders(apiService)
    }

    @Provides
    fun provideUserDataProviders(
        apiService: ApiService,
        tokenManager: TokenManager
    ): UserDataProviders {
        return UserDataProviders(apiService, tokenManager)
    }
}
