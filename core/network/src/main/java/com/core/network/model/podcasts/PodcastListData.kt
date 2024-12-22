package com.core.network.model.podcasts

data class PodcastListPagination(
    var total: Int,
    var per_page: Int,
    var current_page: Int,
    var last_page: Int
)

data class PodcastListData(
    var pagination: PodcastListPagination,
    var items: List<PodcastListItem>
)

