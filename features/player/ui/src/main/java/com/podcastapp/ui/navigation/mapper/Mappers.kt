package com.podcastapp.ui.navigation.mapper

import android.media.MediaMetadataRetriever
import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeFullDTO
import com.doublesymmetry.kotlinaudio.models.AudioItemOptions
import com.doublesymmetry.kotlinaudio.models.DefaultAudioItem
import com.doublesymmetry.kotlinaudio.models.MediaType
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
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()

        durationInSeconds = ((durationInMicroseconds ?: 0) % 60000) / 1000//durationInMicroseconds ?: 0//(durationInMicroseconds ?: 0) / 1000000
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return durationInSeconds
}