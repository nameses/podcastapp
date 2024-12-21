package com.core.network.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class AuthResponse(
    val success: Boolean,
    val data: AuthData,
)

data class AuthData(
    val token: String
)

data class AuthErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)

