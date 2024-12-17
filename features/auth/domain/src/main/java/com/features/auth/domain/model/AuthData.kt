package com.features.auth.domain.model

data class AuthData(
    val success:Boolean = false,
    val token:String? = null
)
