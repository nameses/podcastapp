package com.features.main.ui.navigation.screen

import com.features.main.domain.model.Podcast

data class PodcastStateHolder(
    val isLoading:Boolean = false,
    val data: List<Podcast>? = null,
    val error: String = ""
)
