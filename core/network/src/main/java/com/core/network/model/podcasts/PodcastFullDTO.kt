package com.core.network.model.podcasts

import com.core.network.model.episodes.EpisodeDTO

data class PodcastFullDTO(
    var id: Int,
    var title: String,
    var description: String,
    var image_url: String?,
    var language: String,
    var featured: Boolean,
    var category: CategoryDTO,
    var topics: List<PodcastTopic>,
    var episodes: List<EpisodeDTO>,
    var author: AuthorDTO,
    var is_saved: Boolean
)