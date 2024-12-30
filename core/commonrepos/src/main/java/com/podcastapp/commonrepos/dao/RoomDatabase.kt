package com.podcastapp.commonrepos.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DownloadedEpisode::class, EpisodeTimestamp::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun downloadedEpisodeDao(): DownloadedEpisodeDao
    abstract fun episodeTimestampDao(): EpisodeTimestampDao
}