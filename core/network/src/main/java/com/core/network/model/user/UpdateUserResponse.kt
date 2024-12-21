package com.core.network.model.user

data class UpdateUserResponse(
    val success: Boolean,
    val data: UserDTO
)