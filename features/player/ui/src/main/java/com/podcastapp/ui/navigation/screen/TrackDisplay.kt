package com.podcastapp.ui.navigation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.core.common.theme.ColorLittleBitGray
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.podcastapp.ui.navigation.mapper.millisecondsToString

@Composable
fun TrackDisplay(
    title: String,
    artist: String,
    artwork: String,
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    onSeek: (Long) -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        if (artwork.isEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(ColorWhite).build(),
                contentDescription = "Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(artwork).build(),
                contentDescription = "Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(top = 48.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Column {
            Slider(
                value = if (duration == 0L) {
                    0f
                } else {
                    position.toFloat() / duration.toFloat()
                },
                onValueChange = {
                    onSeek((it * duration).toLong())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                colors = SliderColors(
                    thumbColor = ColorPurple500,
                    activeTrackColor = ColorPurple500,
                    activeTickColor = ColorPurple500,
                    inactiveTrackColor = ColorLittleBitGray,
                    inactiveTickColor = ColorLittleBitGray,
                    disabledThumbColor = ColorLittleBitGray,
                    disabledActiveTrackColor = ColorLittleBitGray,
                    disabledActiveTickColor = ColorLittleBitGray,
                    disabledInactiveTrackColor = ColorLittleBitGray,
                    disabledInactiveTickColor = ColorLittleBitGray
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = position.millisecondsToString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = duration.millisecondsToString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackDisplayPreview() {
    TrackDisplay(
        title = "Title",
        artist = "Artist",
        artwork = "",
        position = 1000,
        duration = 6000,
    )
}

@Preview(showBackground = true)
@Composable
fun TrackDisplayLivePreview() {
    TrackDisplay(
        title = "Title",
        artist = "Artist",
        artwork = "",
        position = 1000,
        duration = 6000,
    )
}