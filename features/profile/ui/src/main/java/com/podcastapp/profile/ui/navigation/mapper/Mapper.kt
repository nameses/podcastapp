package com.podcastapp.profile.ui.navigation.mapper

import com.core.common.ui.HorizontalListItem
import com.podcastapp.profile.domain.model.Podcast

fun List<Podcast>.toHorizontalListItem(): List<HorizontalListItem> {
    return this.map { podcast ->
        HorizontalListItem(podcast.title, podcast.image_url)
    }
}