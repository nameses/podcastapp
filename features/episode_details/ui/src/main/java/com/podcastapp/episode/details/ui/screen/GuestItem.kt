package com.podcastapp.episode.details.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.podcastapp.episode.details.domain.models.Guest

@Composable
fun GuestItem(guest: Guest) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Guest Image
            AsyncImage(
                model = guest.image,
                contentDescription = guest.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            // Guest Name
            Text(
                text = guest.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Job Title
        Text(
            text = guest.jobTitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }

    // Divider
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )
}