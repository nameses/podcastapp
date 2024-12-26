package com.podcastapp.podcast_details.ui.navigation.screen

import androidx.compose.runtime.Composable
import com.podcastapp.podcast_details.ui.navigation.viewmodels.PodcastViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PodcastScreen(
    viewModel: PodcastViewModel, podcastId: Int
) {
    val state by viewModel.state.collectAsState()
    var savedStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPodcast(podcastId)
        if (state.data != null) savedStatus = state.data!!.isSaved
    }

    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (state.isSuccess && state.data != null) {
        PodcastContent(podcast = state.data!!,
            onAddToSavedClick = { viewModel.toggleSaveStatus(podcastId) },
            onPlayClick = {})
    } else {
        Text(
            text = state.message.ifEmpty { "Something went wrong" },
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}