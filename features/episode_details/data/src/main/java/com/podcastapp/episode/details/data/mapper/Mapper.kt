package com.podcastapp.episode.details.data.mapper

import com.core.network.model.episodes.EpisodeDetailedResponse
import com.podcastapp.episode.details.domain.models.Episode
import com.podcastapp.episode.details.domain.models.Guest
import com.core.network.model.episodes.Guest as GuestDTO

fun EpisodeDetailedResponse.toDomainEpisode(): Episode {
    return Episode(
        id = this.data.id,
        title = this.data.title,
        description = this.data.description,
        imageUrl = this.data.image_url,
        fileUrl = this.data.file_path,
        guests = this.data.guests.map { it.toDomainGuest() },
        author = this.data.podcast.author.name,
        isLiked = this.data.is_liked,
        duration = this.data.duration,
        likesAmount = this.data.likes_count,
        language = this.data.podcast.language
    )
}

fun GuestDTO.toDomainGuest(): Guest {
    return Guest(
        id = this.id,
        name = this.name,
        jobTitle = this.job_title,
        image = this.image_url,
    )
}