package com.podcastapp.episode.details.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.podcastapp.episode.details.ui.viewmodels.EpisodeViewModel

@Composable
fun EpisodeScreen(
    navController: NavHostController, viewModel: EpisodeViewModel, episodeId: Int
) {
    val state by viewModel.state.collectAsState()
    var likedStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPodcast(episodeId)
        if (state.data != null) likedStatus = state.data!!.isLiked
    }

    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (state.isSuccess && state.data != null) {
        EpisodeContent(navController = navController,
            episode = state.data!!,
            onLikeClick = { viewModel.toggleLikeStatus(episodeId) })
    } else {
        Text(
            text = state.message.ifEmpty { "Something went wrong" },
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}