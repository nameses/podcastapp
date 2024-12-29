package com.podcastapp.commonrepos.model

data class EpisodeDownload (
    val id: Int,
    val title: String,
    val author: String,
    val fileUrl: String,
    val imageUrl: String
)