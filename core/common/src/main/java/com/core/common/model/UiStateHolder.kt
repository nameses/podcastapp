package com.core.common.model

data class UiStateHolder<T> (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    var data: T? = null,
    val message: String = "",
    val errors: Map<String, List<String>>? = null
)