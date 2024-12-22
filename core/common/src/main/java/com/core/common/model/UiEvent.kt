package com.core.common.model

sealed class UiEvent<T>(
    val data: T? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
) {
    class Loading<T>() : UiEvent<T>()
    class Success<T>(data: T) : UiEvent<T>(data = data)
    class Error<T>(message: String, errors: Map<String, List<String>>? = null) : UiEvent<T>(message = message, errors = errors)
}