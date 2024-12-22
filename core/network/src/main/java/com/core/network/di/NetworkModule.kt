package com.core.network.di

import com.core.common.services.TokenManager
import com.core.network.ApiService
import com.core.network.converterfactories.MultipartConverterFactory
import com.core.network.dataproviders.AuthDataProviders
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.dataproviders.UserDataProviders
import com.core.network.interceptors.AuthTokenInterceptor
import com.core.network.interceptors.HeaderInjectorInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.reflect.Type

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
    fun provideOkHttpClient(
        authTokenInterceptor: AuthTokenInterceptor,
        headerInjectorInterceptor: HeaderInjectorInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInjectorInterceptor)
            .addInterceptor(authTokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthTokenInterceptor(tokenManager: TokenManager): AuthTokenInterceptor {
        return AuthTokenInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideHeaderInjectorInterceptor(): HeaderInjectorInterceptor {
        return HeaderInjectorInterceptor()
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

    @Provides
    fun provideUserDataProviders(apiService: ApiService): UserDataProviders {
        return UserDataProviders(apiService)
    }
}