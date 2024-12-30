package com.podcastapp.search.data.mapper

import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.Topic
import com.podcastapp.search.domain.models.Guest
import com.podcastapp.search.domain.models.Category
import com.core.network.model.podcasts.Topic as ResponseTopic
import com.core.network.model.podcasts.Guest as ResponseGuest
import com.core.network.model.podcasts.Category as ResponseCategory

fun toAvailableFilters(
    topics: List<ResponseTopic>,
    guests: List<ResponseGuest>,
    categories: List<ResponseCategory>
): AvailableFilters {
    return AvailableFilters(
        topics = topics.map { topic ->
            Topic(
                id = topic.id,
                name = topic.name
            )
        },
        categories = categories.map {
            category ->
            Category(
                id = category.id,
                name = category.name
            )
        },
        guests = guests.map { guest ->
            Guest(
                id = guest.id,
                name = guest.name,
                jobTitle = guest.job_title,
                image = guest.image_url
            )
        }
    )
}