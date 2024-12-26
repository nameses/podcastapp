package com.podcastapp.podcast_details.ui.navigation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.core.common.theme.Purple500
import com.podcastapp.podcast_details.domain.model.Podcast


@Composable
fun PodcastContent(
    podcast: Podcast, onAddToSavedClick: () -> Unit, onPlayClick: (Int) -> Unit
) {
    var isSaved by remember { mutableStateOf(podcast.isSaved) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = podcast.imageUrl,
            contentDescription = podcast.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 10f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = podcast.author,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = {
                    onAddToSavedClick()
                    isSaved = !isSaved
                }, modifier = Modifier
                    .background(
                        color = Purple500, shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = "Add to Saved",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Text(
            text = podcast.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )

        Text(
            text = "Episodes:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            podcast.episodes.forEach { episode ->
                EpisodeItem(episode = episode, onPlayClick = onPlayClick)
            }
        }
    }
}