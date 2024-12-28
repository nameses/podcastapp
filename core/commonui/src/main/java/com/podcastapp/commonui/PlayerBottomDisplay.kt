package com.podcastapp.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.core.common.constants.PlayerFeature

@Composable
fun PlayerBottomDisplay(
    navController: NavHostController, episodeId: String
) {
    // Assuming episode data like title, artist, artwork are fetched using the episodeId
    val title = "Episode Title" // Fetch based on episodeId
    val artist = "Artist Name"  // Fetch based on episodeId
    val artwork = ""            // Fetch based on episodeId

    // Small Player
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .background(Color.Gray)
        .clip(RoundedCornerShape(8.dp))
        .clickable { /* Handle click to open full player */ }
        .padding(8.dp)) {
        AsyncImage(
            model = artwork,
            contentDescription = "Artwork",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title)
        Text(text = artist)
        IconButton(onClick = { navController.navigate("${PlayerFeature.playerScreen}}/$episodeId") }) {
            Icon(
                imageVector = Icons.Rounded.PlayCircle, contentDescription = "Play"
            )
        }
    }
}
