package com.podcastapp.commonrepos.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DownloadedEpisode::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun downloadedEpisodeDao(): DownloadedEpisodeDao
}