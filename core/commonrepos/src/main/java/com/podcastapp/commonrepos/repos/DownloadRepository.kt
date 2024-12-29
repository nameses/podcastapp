package com.podcastapp.commonrepos.repos

import com.core.common.model.RepoEvent
import com.podcastapp.commonrepos.dao.DownloadedEpisode
import com.podcastapp.commonrepos.model.EpisodeDownload

interface DownloadRepository {
    suspend fun getEpisodes(): List<DownloadedEpisode>
    suspend fun downloadEpisode(episode: EpisodeDownload): Boolean
    suspend fun cancelDownload(episodeId: Int): Boolean
    suspend fun checkIfExist(episodeId: Int): Boolean
}
