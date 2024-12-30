package com.core.network.model.podcasts

data class CategoryResponseData(
    val success: Boolean,
    val data: List<Category>
)

data class Category(
    val id: Int,
    val name: String
)