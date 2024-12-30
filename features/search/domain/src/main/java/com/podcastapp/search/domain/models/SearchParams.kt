package com.podcastapp.search.domain.models

data class SearchParams(
    val page: Int = 1,
    var search: String?,
    val category: Int?,
    val topics: List<Int>?,
    val guests: List<Int>?,
    val language: Language?,
    val sort: SortOption?,
)

enum class Language(val code: String) {
    UA("ua"),
    EN("en"),
    ES("es"),
    FR("fr"),
    DE("de"),
    IT("it"),
    ZH("zh"),
    JA("ja"),
    KO("ko");

    companion object {
        fun fromCode(code: String): Language? {
            return values().find { it.code == code }
        }
    }
}

enum class SortOption(val value: String, val displayName: String) {
    POPULARITY("popularity", "Popularity (Ascending)"),
    POPULARITY_DESC("-popularity", "Popularity (Descending)"),
    DURATION("duration", "Duration (Shortest First)"),
    DURATION_DESC("-duration", "Duration (Longest First)"),
    UPLOADED_AT("uploaded_at", "Upload Date (Oldest First)"),
    UPLOADED_AT_DESC("-uploaded_at", "Upload Date (Newest First)");

    companion object {
        fun fromValue(value: String): SortOption? {
            return values().find { it.value == value }
        }
    }
}