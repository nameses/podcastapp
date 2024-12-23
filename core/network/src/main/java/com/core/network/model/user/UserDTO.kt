package com.core.network.model.user

data class UserDTO(
    val email: String,
    val username: String,
    val image_url: String?,
    val premium: Boolean
)