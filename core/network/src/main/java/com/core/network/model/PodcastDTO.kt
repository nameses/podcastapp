package com.core.network.model

import java.util.Date

data class PodcastDTO(
    val id: Int,
    val genreIds: List<Int>,
    val author: String,
    val title: String,
    val description: String,
    val imagePath: String,
    val releaseDate: Date
)
