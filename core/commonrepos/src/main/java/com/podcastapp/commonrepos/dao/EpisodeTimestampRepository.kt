package com.podcastapp.commonrepos.dao

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpisodeTimestampRepository(private val context: Context) {

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).fallbackToDestructiveMigration().build()

    private val episodeTimestampDao = db.episodeTimestampDao()

    suspend fun saveEpisodeTimestamp(episodeId: Int, timestamp: Long) {
        withContext(Dispatchers.IO) {
            val episodeTimestamp = EpisodeTimestamp(episodeId, timestamp)
            episodeTimestampDao.upsert(episodeTimestamp)
        }
    }

    suspend fun getTimestampByEpisodeId(episodeId: Int): EpisodeTimestamp? {
        return withContext(Dispatchers.IO) {
            episodeTimestampDao.getTimestampByEpisodeId(episodeId)
        }
    }
}