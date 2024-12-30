package com.core.network.model.podcasts

data class TopicResponseData(
    val success: Boolean,
    val data: List<Topic>
)

data class Topic(
    val id: Int,
    val name: String
)