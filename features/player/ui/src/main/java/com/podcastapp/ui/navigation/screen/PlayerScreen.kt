package com.podcastapp.ui.navigation.screen


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.transition.Slide
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
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
import androidx.compose.ui.draw.rotate
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
import com.core.common.theme.ColorLittleBitGray
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.doublesymmetry.kotlinaudio.models.AudioPlayerState
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import com.podcastapp.ui.R
import com.podcastapp.ui.navigation.mapper.millisecondsToString
import com.podcastapp.ui.navigation.viewmodels.PlayerViewModel
import com.podcastapp.ui.navigation.viewmodels.TimerViewModel
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
    timerViewModel: TimerViewModel = hiltViewModel(),
    episodeId: Int = 0,
    navController : NavHostController
) {
    val playerState =
        viewModel.basePlayer.state.value.event.stateChange.collectAsState(initial = AudioPlayerState.IDLE)
    val player = viewModel.basePlayer.state.collectAsState()
    val title by viewModel.basePlayer.title.collectAsState()
    val artist by viewModel.basePlayer.artist.collectAsState()
    val artwork by viewModel.basePlayer.artwork.collectAsState()
    val isLiked by viewModel.basePlayer.isLiked.collectAsState()

    var position by remember { mutableStateOf(0L) }
    val duration by viewModel.basePlayer.duration.collectAsState()

    val isTimerRunning by timerViewModel.isTimerRunning.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    // For collapsible bottom sheet
    val expandedState = remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = player,
        key2 = player.value.event.audioItemTransition,
        key3 = player.value.event.onPlayerActionTriggeredExternally
    ) {
        viewModel.basePlayer.observePlayer()
    }

    LaunchedEffect(Unit) {
        if (episodeId != 0) {
            viewModel.playEpisode(episodeId)
        }

        while (true) {
            position = player.value.position
            delay(1.seconds / 30)
        }
    }

    if (showDialog.value) {
        TimerDialog(onDismiss = { showDialog.value = false }, onSetTimer = { duration ->
            timerViewModel.startTimer(duration) {
                viewModel.pausePlayback()
            }
        })
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            TrackDisplay(
                title = title,
                artist = artist,
                artwork = artwork,
                position = position,
                duration = duration,
                onSeek = {
                    player.value.seek(it, TimeUnit.MILLISECONDS)
                },
                onBack = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 46.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            PlayerControls(onPrevious = {
                player.value.previous()
            }, onNext = {
                player.value.next()
            }, isPaused = playerState.value != AudioPlayerState.PLAYING, onPlayPause = {
                if (player.value.playerState == AudioPlayerState.PLAYING) {
                    player.value.pause()
                } else {
                    player.value.play()
                }
            }, isTimerRunning = isTimerRunning, onTimerClick = {
                if (isTimerRunning) {
                    timerViewModel.stopTimer()
                } else {
                    showDialog.value = true
                }
            }, onLike = {
                viewModel.likeEpisode()
                viewModel.basePlayer._isLiked.value = !viewModel.basePlayer._isLiked.value
            }, isLiked = isLiked, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
            )

            // Collapsible bottom sheet for "Next Episodes"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .clickable { expandedState.value = !expandedState.value }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Next Episodes",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(
                        onClick = { expandedState.value = !expandedState.value }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Expand/Collapse",
                            modifier = Modifier
                                .rotate(if (expandedState.value) 180f else 0f)
                        )
                    }
                }
            }

            if (expandedState.value) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                ) {
                    //todo
                }
            }
        }
    }
}


