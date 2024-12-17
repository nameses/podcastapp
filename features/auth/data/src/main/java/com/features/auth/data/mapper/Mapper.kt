package com.features.auth.data.mapper

import com.core.network.model.AuthErrorResponse
import com.core.network.model.AuthResponse
import com.features.auth.domain.model.AuthData

fun AuthResponse.toDomainAuthData(): AuthData {
    return AuthData(success, data.token)
}