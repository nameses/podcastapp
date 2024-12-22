package com.podcastapp.profile.domain.model

import java.util.Date

data class Podcast(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String,
    val language: String,
)
