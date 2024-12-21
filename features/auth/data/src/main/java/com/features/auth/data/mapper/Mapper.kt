package com.features.auth.data.mapper

import com.core.network.model.user.AuthResponse
import com.features.auth.domain.model.AuthData

fun AuthResponse.toDomainAuthData(): AuthData {
    return AuthData(success, data.token)
}