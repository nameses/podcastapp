package com.podcastapp.profile.domain.use_cases

import android.util.Log
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
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke(username: String?, image: File) = flow<UiEvent<User>> {
        emit(UiEvent.Loading())
        val params: HashMap<String, RequestBody> = HashMap()

        val imagePart = MultipartBody.Part
            .createFormData(
                "image",
                image.name,
                image.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )

        if(username != null){
            val requestBodyUsername = username.toRequestBody("text/plain".toMediaTypeOrNull())
            params["username"] = requestBodyUsername
        }
        Log.d("TAG", username.toString())
        when (val response = userRepository.editProfile(params, imagePart)) {
            is RepoEvent.Success -> if(response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }
    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}