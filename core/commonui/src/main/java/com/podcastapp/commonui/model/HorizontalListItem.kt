package com.podcastapp.commonui.model

data class HorizontalListItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String?,
    val isInitiallySaved: Boolean
)