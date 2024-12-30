package com.podcastapp.search.domain.models

data class AvailableFilters(
    val topics: List<Topic>,
    val categories: List<Category>,
    val guests: List<Guest>,

    )

data class Topic(
    val id: Int,
    val name: String,
)

data class Category(
    val id: Int,
    val name: String,
)

data class Guest(
    val id: Int,
    val name: String,
    val jobTitle: String,
    val image: String,
)