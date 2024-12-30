package com.podcastapp.ui.navigation.mapper

import android.media.MediaMetadataRetriever
import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeFullDTO
import com.doublesymmetry.kotlinaudio.models.AudioItemOptions
import com.doublesymmetry.kotlinaudio.models.DefaultAudioItem
import com.doublesymmetry.kotlinaudio.models.MediaType
import com.podcastapp.commonrepos.dao.DownloadedEpisode
import java.io.File
import java.io.IOException

fun EpisodeFullDTO.toAudioItem(): DefaultAudioItem {
    return DefaultAudioItem(
        file_path,
        MediaType.DEFAULT,
        title = title,
        artwork = image_url,
        artist = podcast.author.name,
        duration = getMp3DurationInSeconds(file_path) * 1000,
        albumTitle = id.toString()
    )
}

fun EpisodeDTO.toAudioItem(authorName: String): DefaultAudioItem {
    return DefaultAudioItem(
        file_path,
        MediaType.DEFAULT,
        title = title,
        artwork = image_url,
        artist = authorName,
        duration = getMp3DurationInSeconds(file_path) * 1000,
        albumTitle = id.toString()
    )
}

fun DownloadedEpisode.toAudioItem(): DefaultAudioItem {
    return DefaultAudioItem(
        absolutePathMP3,
        MediaType.DEFAULT,
        title = title,
        artwork = absolutePathImage,
        artist = author,
        duration = getMp3DurationInSecondsFromFile(absolutePathMP3) * 1000,
        albumTitle = id.toString()
    )
}

fun Long.millisecondsToString(): String {
    val seconds = this / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}


fun getMp3DurationInSeconds(fileUrl: String): Long {
    val retriever = MediaMetadataRetriever()
    var durationInSeconds = 0.toLong()

    try {
        retriever.setDataSource(fileUrl)

        val durationInMicroseconds =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0

        val zero = 0
        durationInSeconds = if(durationInMicroseconds == zero.toLong()) 0 else (durationInMicroseconds / 1_000)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return durationInSeconds
}

fun getMp3DurationInSecondsFromFile(filePath: String): Long {
    val retriever = MediaMetadataRetriever()
    var durationInSeconds = 0L

    try {
        retriever.setDataSource(filePath)//todo check

        val durationInMicroseconds =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L

        durationInSeconds = if (durationInMicroseconds == 0L) 0 else (durationInMicroseconds / 1_000)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return durationInSeconds
}