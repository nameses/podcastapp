package com.podcastapp.commonrepos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DownloadedEpisodeDao {

    @Insert
    suspend fun insert(episode: DownloadedEpisode)

    @Query("SELECT * FROM DownloadedEpisodes WHERE id = :id LIMIT 1")
    suspend fun getEpisodeById(id: Int): DownloadedEpisode?

    @Query("SELECT * FROM DownloadedEpisodes")
    suspend fun getEpisodes(): List<DownloadedEpisode>?

    @Query("DELETE FROM DownloadedEpisodes WHERE id = :episodeId")
    suspend fun deleteEpisodeById(episodeId: Int)

    @Query("SELECT 1 FROM DownloadedEpisodes WHERE id = :episodeId")
    suspend fun checkIfExist(episodeId: Int): Int?
}