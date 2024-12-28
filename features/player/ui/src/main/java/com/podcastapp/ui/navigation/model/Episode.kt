package com.podcastapp.ui.navigation.model

import kotlin.time.Duration

data class Episode (
    val id:Int,
    val title:String,
    val description:String,
    val imageUrl:String,
    val duration: String
)