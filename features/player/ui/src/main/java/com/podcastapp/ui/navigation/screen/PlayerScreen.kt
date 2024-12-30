package com.podcastapp.ui.navigation.screen


import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.core.common.constants.PlayerFeature
import com.core.common.constants.ProfileFeature
import com.core.common.theme.ColorWhite
import com.core.network.model.episodes.EpisodeDTO
import com.doublesymmetry.kotlinaudio.models.AudioItem
import com.doublesymmetry.kotlinaudio.models.AudioPlayerState
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import com.podcastapp.commonrepos.model.EpisodeDownload
import com.podcastapp.commonui.download.DownloadButton
import com.podcastapp.commonui.errorscreen.ErrorScreen
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
    navController: NavHostController
) {
    val loadState = viewModel.loadState.collectAsState()
    val playerState =
        viewModel.basePlayer.state.value.event.stateChange.collectAsState(initial = AudioPlayerState.IDLE)
    val player = viewModel.basePlayer.state.collectAsState()
    val title by viewModel.basePlayer.title.collectAsState()
    val artist by viewModel.basePlayer.artist.collectAsState()
    val artwork by viewModel.basePlayer.artwork.collectAsState()
    val isLiked by viewModel.basePlayer.isLiked.collectAsState()
    val id by viewModel.basePlayer.id.collectAsState()
    val audioUrl by viewModel.basePlayer.audioUrl.collectAsState()

    var position by remember { mutableStateOf(0L) }
    val duration by viewModel.basePlayer.duration.collectAsState()

    val isTimerRunning by timerViewModel.isTimerRunning.collectAsState()
    val showTimerDialog = remember { mutableStateOf(false) }

    val isExpanded = remember { mutableStateOf(false) }

    val nextEpisodes by viewModel.basePlayer.nextEpisodesItems.collectAsState()

    BackHandler {
        if (navController.previousBackStackEntry?.destination?.route == ProfileFeature.profileScreen
            || navController.previousBackStackEntry?.destination?.route == ProfileFeature.profileScreenDeepLink )
        {
            navController.navigate(
                Uri.parse(ProfileFeature.profileScreenDeepLink)
            )
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        if (episodeId != 0) {
            viewModel.playEpisode(episodeId)
        }

        while (true) {
            position = player.value.position
            delay(1.seconds / 20)
        }
    }

    if (loadState.value.isLoading) {
        PlayerScreenSilhouette()
    } else if (loadState.value.isSuccess || episodeId == 0) {
        if (showTimerDialog.value) {
            TimerDialog(onDismiss = { showTimerDialog.value = false }, onSetTimer = { duration ->
                timerViewModel.startTimer(duration) {
                    viewModel.pausePlayback()
                }
            })
        }

        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(title = { Text(text = "") }, navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry?.destination?.route == ProfileFeature.profileScreen
                            || navController.previousBackStackEntry?.destination?.route == ProfileFeature.profileScreenDeepLink )
                        {
                            navController.navigate(
                                Uri.parse(ProfileFeature.profileScreenDeepLink)
                            )
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Back")
                    }
                }, actions = {
                    DownloadButton(
                        EpisodeDownload(
                            (if (id == "") 0 else id.toInt()), title, artist, audioUrl, artwork
                        )
                    )
                }, modifier = Modifier.fillMaxWidth()
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
                        showTimerDialog.value = true
                    }
                }, onLike = {
                    viewModel.likeEpisode()
                    viewModel.basePlayer._isLiked.value = !viewModel.basePlayer._isLiked.value
                }, isLiked = isLiked, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp)
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .animateContentSize()
                    .clickable { isExpanded.value = true }) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 0.dp, horizontal = 16.dp)
                                .clickable { isExpanded.value = !isExpanded.value },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next episodes",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Expand/Collapse"
                            )
                        }
                    }
                }

                if (isExpanded.value) {
                    ShowNextEpisodesDialog(nextEpisodes = nextEpisodes, onDismiss = {
                        isExpanded.value = false
                    }, viewModel)
                }
            }
        }
    } else {
        ErrorScreen()
    }
}

@Composable
fun ShowNextEpisodesDialog(
    nextEpisodes: List<EpisodeDTO>, onDismiss: () -> Unit, viewModel: PlayerViewModel
) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnClickOutside = true, usePlatformDefaultWidth = false
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clickable { onDismiss() }) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp, max = LocalConfiguration.current.screenHeightDp.dp / 2)
                    .align(Alignment.BottomCenter)
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    color = ColorWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Next episodes",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        if (!nextEpisodes.isEmpty()) {
                            LazyColumn {
                                items(nextEpisodes) { episode ->
                                    EpisodeItem(episode, onPlayClick = {
                                        viewModel.playEpisode(episode.id)
                                        onDismiss()
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

