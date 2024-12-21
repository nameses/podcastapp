package com.core.network.model.common

data class ValidationErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)