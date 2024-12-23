package com.podcastapp.profile.domain.use_cases

import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.podcastapp.profile.domain.model.User
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke(username: String, image: File) = flow<UiEvent<User>> {
        emit(UiEvent.Loading())

        val requestBodyUsername = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = image.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        when (val response = userRepository.editProfile(requestBodyUsername, imagePart)) {
            is RepoEvent.Success -> if(response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}