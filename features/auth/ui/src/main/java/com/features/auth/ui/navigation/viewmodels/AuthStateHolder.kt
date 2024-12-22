package com.features.auth.ui.navigation.viewmodels

import com.features.auth.domain.model.AuthData
import com.features.auth.domain.model.AuthResult

data class AuthStateHolder(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val data: AuthData? = null,
    val message: String = "",
    val errors: Map<String, List<String>>? = null
)