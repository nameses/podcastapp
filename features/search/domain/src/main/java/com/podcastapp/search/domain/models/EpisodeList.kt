package com.podcastapp.search.domain.models

data class EpisodePagination(
    var total: Int,
    var perPage: Int,
    var currentPage: Int,
    var lastPage: Int
)

data class EpisodeList(
    var pagination: EpisodePagination,
    var items: List<SearchedEpisode>
)
