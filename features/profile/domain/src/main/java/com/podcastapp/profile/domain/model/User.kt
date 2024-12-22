package com.podcastapp.profile.domain.model

data class User(
    val email: String,
    val username: String,
    val image_url: String,
    val premium: Boolean
)