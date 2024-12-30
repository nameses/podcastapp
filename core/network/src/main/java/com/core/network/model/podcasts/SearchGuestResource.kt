package com.core.network.model.podcasts

// Root response class
data class GuestResponseData(
    val success: Boolean,
    val data: List<Guest>
)

// Represents each item in the data array
data class Guest(
    val id: Int,
    val name: String,
    val job_title: String,
    val image_url: String
)