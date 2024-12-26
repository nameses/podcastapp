package com.podcastapp.profile.domain.model

import java.util.Date

data class Podcast(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String?,
    val isSaved: Boolean = true
)
