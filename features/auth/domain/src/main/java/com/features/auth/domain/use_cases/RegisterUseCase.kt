package com.features.auth.domain.use_cases

import com.core.common.model.UiEvent
import com.features.auth.domain.model.AuthData
import com.features.auth.domain.model.AuthResult
import com.features.auth.domain.repo.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(username: String, email: String, password: String) =
        flow<UiEvent<AuthData>> {
            emit(UiEvent.Loading())

            when (val response = authRepository.register(username, email, password)) {
                is AuthResult.Success -> emit(UiEvent.Success(response.data))
                is AuthResult.Error -> emit(UiEvent.Error(response.message, response.errors))
                is AuthResult.Loading -> emit(UiEvent.Loading())
            }
        }.catch {
            emit(UiEvent.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}