package com.podcastapp.episode.details.ui.screen

import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.core.common.constants.PlayerFeature
import com.core.common.constants.ProfileFeature
import com.core.common.theme.ColorPurple500
import com.podcastapp.commonrepos.model.EpisodeDownload
import com.podcastapp.commonui.download.DownloadButton
import com.podcastapp.episode.details.domain.models.Episode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeContent(
    navController: NavHostController, episode: Episode, onLikeClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(episode.isLiked) }
    var likeCount by remember { mutableIntStateOf(episode.likesAmount) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopAppBar(title = { Text(text = "") }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
        }, actions = {
            DownloadButton(
                EpisodeDownload(
                    episode.id,
                    episode.title,
                    episode.author,
                    episode.fileUrl,
                    episode.imageUrl ?: ""
                )
            )
        }, modifier = Modifier.fillMaxWidth().height(32.dp)
        )

        AsyncImage(
            model = episode.imageUrl,
            contentDescription = episode.title,
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
                    text = episode.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = episode.author,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = {
                    navController.navigate("${PlayerFeature.playerScreen}/${episode.id}")
                }, modifier = Modifier
                    .background(
                        color = ColorPurple500, shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play episode",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Like Button with Like Count
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        onLikeClick()
                        isLiked = !isLiked
                        if (isLiked) {
                            likeCount++
                        } else {
                            likeCount--
                        }
                    },
                    modifier = Modifier
                        .background(color = ColorPurple500, shape = CircleShape)
                        .padding(4.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = likeCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "${episode.duration / 60}h ${episode.duration % 60}m",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = episode.language.uppercase(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold

            )
        }

        Text(
            text = episode.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )

        Text(
            text = "Guests:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            episode.guests.forEach { guest ->
                GuestItem(guest)
            }
        }
    }
}