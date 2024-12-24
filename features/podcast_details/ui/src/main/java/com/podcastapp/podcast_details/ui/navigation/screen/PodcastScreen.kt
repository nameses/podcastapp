package com.podcastapp.podcast_details.ui.navigation.screen

import androidx.compose.runtime.Composable
import com.podcastapp.podcast_details.ui.navigation.viewmodels.PodcastViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.podcastapp.podcast_details.domain.model.Episode
import com.podcastapp.podcast_details.domain.model.Podcast
import com.podcastapp.podcast_details.ui.R

@Composable
fun PodcastScreen(
    viewModel: PodcastViewModel,
    podcastId: Int
) {
    val state by viewModel.state.collectAsState()
    var savedStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPodcast(podcastId)
        if(state.data != null)
            savedStatus = state.data!!.isSaved
    }

    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (state.isSuccess && state.data != null) {
        PodcastContent(podcast = state.data!!, onAddToSavedClick = {
            viewModel.toggleSaveStatus(podcastId)
        }, onPlayClick = { /*episodeId ->
            viewModel.playEpisode(episodeId)
        */ })
    } else {
        Text(
            text = state.message.ifEmpty { "Something went wrong" },
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PodcastContent(
    podcast: Podcast,
    onAddToSavedClick: () -> Unit,
    onPlayClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Podcast Image
        AsyncImage(
            model = podcast.imageUrl,
            contentDescription = podcast.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 10f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Title, Author, and Save Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = podcast.author,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = onAddToSavedClick,
                modifier = Modifier
                    .background(
                        color = Color(0xFF6200EE), // Purple background
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Add to Saved",
                    tint = Color.White
                )
            }
        }

        // Description
        Text(
            text = podcast.description,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start
        )

        // Episodes Header
        Text(
            text = "Episodes:",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start
        )

        // Episodes List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(podcast.episodes) { episode ->
                EpisodeItem(episode = episode, onPlayClick = onPlayClick)
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: Episode,
    onPlayClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Episode Image
        AsyncImage(
            model = episode.imageUrl,
            contentDescription = episode.title,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = episode.description,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }

        // Play Button
        Button(
            onClick = { onPlayClick(episode.id) },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE))
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White
            )
        }
    }
}
