package com.features.auth.domain.model

sealed class AuthResult {
    data class Success(val data: AuthData) : AuthResult()
    data class Error(val message: String, val errors: Map<String, List<String>>?) : AuthResult()
    object Loading : AuthResult()
}