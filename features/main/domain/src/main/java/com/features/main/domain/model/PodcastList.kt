package com.features.main.domain.model

data class PodcastPagination(
    var total: Int,
    var perPage: Int,
    var currentPage: Int,
    var lastPage: Int
)

data class PodcastList(
    var pagination: PodcastPagination,
    var items: List<PodcastDTO>
)
