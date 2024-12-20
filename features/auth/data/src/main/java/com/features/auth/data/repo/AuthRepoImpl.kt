package com.features.auth.data.repo

import com.core.network.dataproviders.AuthDataProviders
import com.core.network.model.AuthErrorResponse
import com.core.network.model.LoginRequest
import com.core.network.model.RegisterRequest
import com.features.auth.data.mapper.toDomainAuthData
import com.features.auth.domain.model.AuthResult
import com.features.auth.domain.repo.AuthRepository
import com.google.gson.Gson
import javax.inject.Inject

class AuthRepoImpl
@Inject constructor(private val authDataProvider: AuthDataProviders) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = authDataProvider.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AuthResult.Success(body.toDomainAuthData())
                } else {
                    AuthResult.Error("Unexpected response", null)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, AuthErrorResponse::class.java)
                }
                AuthResult.Error(
                    message = errorResponse?.message ?: "Unknown error",
                    errors = errorResponse?.errors
                )
            }
        } catch (e: Exception) {
            AuthResult.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                errors = null
            )
        }
    }

    override suspend fun register(username: String, email: String, password: String): AuthResult {
        return try {
            val response = authDataProvider.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AuthResult.Success(body.toDomainAuthData())
                } else {
                    AuthResult.Error("Unexpected response", null)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, AuthErrorResponse::class.java)
                }
                AuthResult.Error(
                    message = errorResponse?.message ?: "Unknown error",
                    errors = errorResponse?.errors
                )
            }
        } catch (e: Exception) {
            AuthResult.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                errors = null
            )
        }
    }
}