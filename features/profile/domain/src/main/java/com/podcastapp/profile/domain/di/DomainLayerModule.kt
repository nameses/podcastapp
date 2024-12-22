package com.podcastapp.profile.domain.di

import com.podcastapp.profile.domain.repo.UserRepository
import com.podcastapp.profile.domain.use_cases.EditProfileUseCase
import com.podcastapp.profile.domain.use_cases.ProfileUseCase
import com.podcastapp.profile.domain.use_cases.PurchasePremiumUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {
    @Provides
    fun provideEditProfileUseCase(userRepository: UserRepository): EditProfileUseCase {
        return EditProfileUseCase(userRepository)
    }

    @Provides
    fun provideProfileUseCase(userRepository: UserRepository): ProfileUseCase {
        return ProfileUseCase(userRepository)
    }

    @Provides
    fun providePurchasePremiumUseCase(userRepository: UserRepository): PurchasePremiumUseCase {
        return PurchasePremiumUseCase(userRepository)
    }
}