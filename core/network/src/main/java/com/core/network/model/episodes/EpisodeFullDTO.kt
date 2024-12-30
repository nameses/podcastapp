package com.core.network.model.episodes

data class EpisodeFullDTO(
    var id: Int,
    val title: String,
    var description: String,
    var image_url: String,
    var duration: Int,
    var episode_number: Int,
    var file_path: String,
    var category: Category,
    var topics: List<Topic>,
    var guests: List<Guest>,
    var podcast: PodcastDTO,
    var next_episodes: List<EpisodeDTO>,
    var is_liked: Boolean,
    var likes_count: Int
)

data class Category(
    val id: Int, val name: String
)

data class Topic(
    val id: Int, val name: String
)

data class Guest(
    val id: Int, val name: String, val job_title: String, val image_url: String
)