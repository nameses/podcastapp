package com.podcastapp.profile.ui.navigation.mapper

import com.podcastapp.commonui.HorizontalListItem
import com.podcastapp.profile.domain.model.Podcast

fun List<Podcast>.toHorizontalListItem(): List<com.podcastapp.commonui.HorizontalListItem> {
    return this.map { podcast ->
        com.podcastapp.commonui.HorizontalListItem(podcast.title, podcast.image_url)
    }
}