package com.podcastapp.search.data.mapper

import com.podcastapp.search.domain.models.SearchParams

fun SearchParams.toQueryMap(): Map<String, String> {
    val queryMap = mutableMapOf<String, String>()

    queryMap["page"] = page.toString()

    search?.let { queryMap["filter[search]"] = it }
    category?.let { queryMap["filter[category]"] = it.toString() }
    topics?.takeIf { it.isNotEmpty() }?.let { queryMap["filter[topics]"] = it.joinToString(",") }
    guests?.takeIf { it.isNotEmpty() }?.let { queryMap["filter[guests]"] = it.joinToString(",") }
    language?.let { queryMap["filter[language]"] = it.code }
    sort?.let { queryMap["sort"] = it.value }

    return queryMap
}