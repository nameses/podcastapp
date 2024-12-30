package com.podcastapp.ui.navigation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.common.services.isNetworkAvailable
import com.core.common.theme.ColorLittleBitGray
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    isPaused: Boolean,
    onPlayPause: () -> Unit = {},
    isTimerRunning: Boolean = false,
    onTimerClick: () -> Unit = {},
    isLiked: Boolean = false,
    onLike: () -> Unit = {},
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        IconButton(
            enabled = isNetworkAvailable(context),
            onClick = onLike,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ColorWhite, contentColor = if(isNetworkAvailable(context)) ColorPurple500 else Color.Gray
            )
        ) {
            Icon(
                imageVector = if(isNetworkAvailable(context)){
                    if (isLiked)
                        Icons.Default.Favorite
                    else
                        Icons.Filled.FavoriteBorder
                } else Icons.Default.Favorite,
                contentDescription = "Like",
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = onPrevious,
            modifier = Modifier
                .height(48.dp)
                .width(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ColorWhite, contentColor = ColorPurple500
            )
        ) {
            Icon(
                Icons.Rounded.FastRewind,
                contentDescription = "Previous",
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .height(68.dp)
                .width(68.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ColorWhite, contentColor = ColorPurple500
            )
        ) {
            Icon(
                if (isPaused) {
                    Icons.Rounded.PlayCircle
                } else {
                    Icons.Rounded.PauseCircle
                },
                contentDescription = "Play",
                modifier = Modifier
                    .height(68.dp)
                    .width(68.dp)
                    .clip(RoundedCornerShape(50))
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(
            onClick = onNext,
            modifier = Modifier
                .height(48.dp)
                .width(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ColorWhite, contentColor = ColorPurple500
            )
        ) {
            Icon(
                Icons.Rounded.FastForward,
                contentDescription = "Next",
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = onTimerClick,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ColorWhite, contentColor = ColorPurple500
            )
        ) {
            Icon(
                imageVector = if (isTimerRunning) Icons.Filled.TimerOff else Icons.Filled.Timer,
                contentDescription = "Timer",
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerControlsPreview() {
    Column {
        PlayerControls(isPaused = true)
        PlayerControls(isPaused = false)
    }
}