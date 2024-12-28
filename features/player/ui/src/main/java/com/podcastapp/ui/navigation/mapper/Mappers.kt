package com.podcastapp.ui.navigation.mapper

import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeFullDTO
import com.doublesymmetry.kotlinaudio.models.DefaultAudioItem
import com.doublesymmetry.kotlinaudio.models.MediaType

fun EpisodeFullDTO.toAudioItem(): DefaultAudioItem {
    return DefaultAudioItem(
        file_path,
        MediaType.DEFAULT,
        title = title,
        artwork = image_url,
        artist = podcast.author.name,
        duration = duration.toLong() * 1000,
    )
}

fun EpisodeDTO.toAudioItem(authorName: String): DefaultAudioItem {
    return DefaultAudioItem(
        file_path,
        MediaType.DEFAULT,
        title = title,
        artwork = image_url,
        artist = authorName,
        duration = duration.toLong() * 1000,
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