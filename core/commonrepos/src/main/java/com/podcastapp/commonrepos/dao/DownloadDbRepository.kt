package com.podcastapp.commonrepos.dao

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadDbRepository(private val context: Context) {

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()

    private val downloadedEpisodeDao = db.downloadedEpisodeDao()

    suspend fun saveEpisode(episode: DownloadedEpisode) {
        withContext(Dispatchers.IO) {
            downloadedEpisodeDao.insert(episode)
        }
    }

    suspend fun getEpisodes(): List<DownloadedEpisode> {
        return withContext(Dispatchers.IO) {
            downloadedEpisodeDao.getEpisodes() ?: emptyList()
        }
    }
    suspend fun getEpisodeById(id: Int): DownloadedEpisode? {
        return withContext(Dispatchers.IO) {
            downloadedEpisodeDao.getEpisodeById(id)
        }
    }

    suspend fun deleteEpisodeById(episodeId: Int) {
        return withContext(Dispatchers.IO) {
            downloadedEpisodeDao.deleteEpisodeById(episodeId)
        }
    }

    suspend fun checkIfExist(episodeId: Int): Int {
        return withContext(Dispatchers.IO) {
            downloadedEpisodeDao.checkIfExist(episodeId) ?: 0
        }
    }
}