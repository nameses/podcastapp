package com.podcastapp.profile.domain.use_cases

import android.util.Log
import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = flow<UiEvent<UserFull>> {
        emit(UiEvent.Loading())

        when (val response = userRepository.getProfile()) {
            is RepoEvent.Success -> if(response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}