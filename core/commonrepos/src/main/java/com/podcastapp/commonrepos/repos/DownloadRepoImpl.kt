package com.podcastapp.commonrepos.repos

import android.content.Context
import com.core.common.model.RepoEvent
import com.core.network.dataproviders.PodcastDataProviders
import com.core.network.model.common.ValidationErrorResponse
import com.google.gson.Gson
import com.podcastapp.commonrepos.dao.DownloadDbRepository
import com.podcastapp.commonrepos.dao.DownloadedEpisode
import com.podcastapp.commonrepos.model.EpisodeDownload
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Inject
import java.io.IOException
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadRepoImpl
@Inject constructor(
    private val context: Context, private val downloadDbRepository: DownloadDbRepository
) : DownloadRepository {

    private val client = OkHttpClient()

    override suspend fun downloadEpisode(episode: EpisodeDownload): Boolean {
        val mp3FileName = "${episode.id}.mp3"
        val mp3File = File(context.filesDir, mp3FileName)

        val imageFileName = "${episode.id}.jpg"
        val imageFile = File(context.filesDir, imageFileName)

        try {
            withContext(Dispatchers.IO) {
                val mp3Request = Request.Builder().url(episode.fileUrl).build()
                val mp3Response = client.newCall(mp3Request).execute()

                if (mp3Response.isSuccessful) {
                    mp3Response.body?.byteStream()?.use { inputStream ->
                        mp3File.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                } else {
                    throw IOException("Failed to download MP3 file.")
                }

                val imageRequest = Request.Builder().url(episode.imageUrl).build()
                val imageResponse = client.newCall(imageRequest).execute()

                if (imageResponse.isSuccessful) {
                    imageResponse.body?.byteStream()?.use { inputStream ->
                        imageFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                } else {
                    throw IOException("Failed to download image file.")
                }
            }

            val downloadedEpisode = DownloadedEpisode(
                id = episode.id,
                title = episode.title,
                author = episode.author,
                absolutePathMP3 = mp3File.absolutePath,
                absolutePathImage = imageFile.absolutePath
            )

            downloadDbRepository.saveEpisode(downloadedEpisode)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun cancelDownload(episodeId: Int): Boolean {
        val fileNameMP3 = "$episodeId.mp3"
        val fileMP3 = File(context.filesDir, fileNameMP3)
        val fileNameImage = "$episodeId.mp3"
        val fileImage = File(context.filesDir, fileNameImage)

        return try {
            if (fileMP3.exists()) {
                fileMP3.delete()
            }

            if (fileImage.exists()) {
                fileImage.delete()
            }

            downloadDbRepository.deleteEpisodeById(episodeId)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun checkIfExist(episodeId: Int): Boolean {
        return downloadDbRepository.checkIfExist(episodeId) == 1
    }

    override suspend fun getEpisodes(): List<DownloadedEpisode> {
        return downloadDbRepository.getEpisodes()
    }

    override suspend fun getEpisodeById(id: Int): DownloadedEpisode {
        return downloadDbRepository.getEpisodeById(id) ?: DownloadedEpisode(0, "", "", "", "")
    }
}