package com.podcastapp.podcast_details.data.mapper

import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.podcasts.PodcastDetailedResponse
import com.podcastapp.podcast_details.domain.model.Episode
import com.podcastapp.podcast_details.domain.model.Podcast


fun PodcastDetailedResponse.toDomainPodcast(): Podcast {
    return Podcast(
        id = this.data.id,
        title = this.data.title,
        description = this.data.description,
        imageUrl = this.data.image_url,
        episodes = this.data.episodes.map { it.toDomainEpisode() },
        author = this.data.author.name,
        isSaved = this.data.is_saved
    )
}

fun EpisodeDTO.toDomainEpisode(): Episode {
    return Episode(
        id = this.id,
        title = this.title,
        description = this.description,
        duration = this.duration,
        imageUrl = this.image_url,
        filePath = this.file_path
    )
}
