package com.podcastapp.commonrepos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeTimestampDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(episode: EpisodeTimestamp)

    @Query("SELECT * FROM EpisodeTimestamps WHERE episodeId = :episodeId LIMIT 1")
    suspend fun getTimestampByEpisodeId(episodeId: Int): EpisodeTimestamp?
}