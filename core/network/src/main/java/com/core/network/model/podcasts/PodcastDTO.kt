package com.core.network.model.podcasts

data class PodcastDTO(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String?,
    val language: String,
    val featured: Boolean,
    //val author: Author,
    val is_saved: Boolean,
)

//data class Author(
//    val name: String
//)