package com.podcastapp.commonrepos.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DownloadedEpisodes")
data class DownloadedEpisode(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val absolutePathMP3: String,
    val absolutePathImage: String,
)

@Entity(tableName = "EpisodeTimestamps")
data class EpisodeTimestamp(
    @PrimaryKey val episodeId: Int,
    val timestamp: Long
)