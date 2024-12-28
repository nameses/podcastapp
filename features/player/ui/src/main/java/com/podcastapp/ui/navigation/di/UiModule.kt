package com.podcastapp.ui.navigation.di

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.ui.navigation.PlayerApi
import com.podcastapp.ui.navigation.PlayerApiImpl
import com.podcastapp.ui.navigation.viewmodels.BasePlayerViewModel
import com.podcastapp.ui.navigation.viewmodels.PlayerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun providePlayerApi(): PlayerApi {
        return PlayerApiImpl()
    }

    @Singleton
    @Provides
    fun provideBasePlayerViewModel(
        context: Context, commonEpisodeRepository: CommonEpisodeRepository
    ): BasePlayerViewModel {
        return BasePlayerViewModel(context, commonEpisodeRepository)
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext
}