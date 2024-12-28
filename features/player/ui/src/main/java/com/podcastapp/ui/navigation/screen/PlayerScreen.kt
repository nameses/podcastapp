package com.podcastapp.ui.navigation.screen


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.core.common.constants.PodcastDetailedFeature
import com.core.common.constants.ProfileFeature
import com.core.common.services.setNavResultCallback
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.doublesymmetry.kotlinaudio.models.AudioPlayerState
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import com.podcastapp.ui.R
import com.podcastapp.ui.navigation.mapper.millisecondsToString
import com.podcastapp.ui.navigation.viewmodels.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    episodeId: Int = 0,
) {
    val playerState =
        viewModel.basePlayer.state.value.event.stateChange.collectAsState(initial = AudioPlayerState.IDLE)
    val player = viewModel.basePlayer.state.collectAsState()
    val title by viewModel.basePlayer.title.collectAsState()
    val artist by viewModel.basePlayer.artist.collectAsState()
    val artwork by viewModel.basePlayer.artwork.collectAsState()

    var position by remember { mutableStateOf(0L) }
    val duration by viewModel.basePlayer.duration.collectAsState()

    LaunchedEffect(
        key1 = player,
        key2 = player.value.event.audioItemTransition,
        key3 = player.value.event.onPlayerActionTriggeredExternally
    ) {
        viewModel.observePlayer()
    }

    LaunchedEffect(Unit) {
        if (episodeId != 0) {
            viewModel.playEpisode(episodeId)
        }

//        viewModel.startSavingEpisodeDataEverySecond()

//        else {
//            viewModel.retrieveLastPlayedEpisode()
//        }
//
//        viewModel.startSavingEpisodeDataEverySecond()

        while (true) {
            position = player.value.position

            delay(1.seconds / 30)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TrackDisplay(
                title = title,
                artist = artist,
                artwork = artwork,
                position = position,
                duration = duration,
                onSeek = {
                    player.value.seek(it, TimeUnit.MILLISECONDS)
                },
                modifier = Modifier.padding(top = 46.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            PlayerControls(onPrevious = {
                player.value.previous()
            }, onNext = {
                player.value.next()
            }, isPaused = playerState?.value != AudioPlayerState.PLAYING, onPlayPause = {
                if (player.value.playerState == AudioPlayerState.PLAYING) {
                    player.value.pause()
                } else {
                    player.value.play()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
            )
        }
    }
}

//
//ActionBottomSheet
//

@Composable
@ExperimentalMaterial3Api
fun ActionBottomSheet(
    onDismiss: () -> Unit,
    onRandomMetadata: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        InnerSheet(onRandomMetadata = onRandomMetadata)
    }
}

@Composable
fun InnerSheet(onRandomMetadata: () -> Unit = {}) {
    // Add a button to perform an action when clicked
    Button(
        onClick = onRandomMetadata, modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Metadata: Update Randomly")
    }
}

@Preview
@ExperimentalMaterial3Api
@Composable
fun ActionBottomSheetPreview() {
    InnerSheet()
}

//
//PlayerControls
//
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    isPaused: Boolean,
    onPlayPause: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        IconButton(
            onClick = onPrevious, modifier = Modifier
                .height(48.dp)
                .width(48.dp)
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
            onClick = onPlayPause, modifier = Modifier
                .height(68.dp)
                .width(68.dp)
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
            onClick = onNext, modifier = Modifier
                .height(48.dp)
                .width(48.dp)
        ) {
            Icon(
                Icons.Rounded.FastForward,
                contentDescription = "Next",
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
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

//
//
//
@OptIn(ExperimentalCoilApi::class)
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
                contentDescription = "Album Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(artwork).build(),
                contentDescription = "Album Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(top = 48.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.bodyMedium,
        )
        Column {
            Slider(
                value = if (duration == 0L) {
                    0f
                } else {
                    position.toFloat() / duration.toFloat()
                }, onValueChange = {
                    onSeek((it * duration).toLong())
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
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




