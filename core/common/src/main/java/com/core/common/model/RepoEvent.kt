package com.core.common.model

sealed class RepoEvent<T>(
    val data: T? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
) {
    class Success<T>(data: T) : RepoEvent<T>(data = data)
    class Error<T>(message: String, errors: Map<String, List<String>>? = null) : RepoEvent<T>(message = message, errors = errors)
}