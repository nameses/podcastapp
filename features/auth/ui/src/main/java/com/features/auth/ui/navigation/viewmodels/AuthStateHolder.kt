package com.features.auth.ui.navigation.viewmodels

import com.features.auth.domain.model.AuthData
import com.features.auth.domain.model.AuthResult

data class AuthStateHolder(
    val isLoading: Boolean = false,
    val data: AuthData? = null,
    val errors: Map<String, List<String>>? = null,
    val message: String = ""
)