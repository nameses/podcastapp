package com.core.network.model.user

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

