package com.podcastapp.profile.ui.navigation.screen

import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import coil3.annotation.ExperimentalCoilApi
import com.podcastapp.profile.ui.navigation.viewmodels.ProfileViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.core.common.constants.EpisodeDetailedFeature
import com.core.common.constants.PlayerFeature
import com.core.common.constants.PodcastDetailedFeature
import com.core.common.constants.ProfileFeature
import com.core.common.services.TokenManager
import com.core.common.services.isNetworkAvailable
import com.core.common.services.setNavResultCallback
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.podcastapp.commonui.HorizontalList
import com.podcastapp.commonui.errorscreen.ErrorScreen
import com.podcastapp.profile.ui.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController, viewModel: ProfileViewModel, onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val ifContainsLikedEpisodes by viewModel.ifContainsLikedEpisodes.collectAsState()
    val ifContainsSavedPodcasts by viewModel.ifContainsSavedPodcasts.collectAsState()
    val ifContainsDownloadedEpisodes by viewModel.ifContainsDownloadedEpisodes.collectAsState()
    var offset by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scrollable(orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                })
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.isSuccess -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorPurple500)
                            .height(240.dp),
                        ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(ColorPurple500)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(ColorWhite)
                                .align(Alignment.BottomCenter)
                        )

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.data?.imageUrl ?: R.drawable.default_avatar)
                                .crossfade(true).build(),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .border(4.dp, ColorWhite, CircleShape)
                                .align(Alignment.BottomCenter)
                        )


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                val isNetworkAvailable = LocalContext.current
                                Text(text = "Edit",
                                    color = ColorWhite,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .clickable {
                                            if(isNetworkAvailable(isNetworkAvailable))
                                                navController.navigate(ProfileFeature.profileEditScreen)
                                        }
                                        .align(Alignment.CenterStart)
                                        .padding(start = 8.dp))
                            }

                            Box(
                                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Profile",
                                    color = ColorWhite,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(text = "Logout",
                                    color = ColorWhite,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.clearToken()
                                            onLogout()
                                        }
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 8.dp),
                                    overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = state.data?.username ?: "",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val podcastsListState by viewModel.savedPodcasts.collectAsState()
                    val podcastsLazyListState = rememberLazyListState()

                    val episodesListState by viewModel.likedEpisodes.collectAsState()
                    val episodesLazyListState = rememberLazyListState()

                    val downloadedEpisodesListState by viewModel.downloadedEpisodes.collectAsState()
                    val downloadedEpisodesLazyListState = rememberLazyListState()

                    val handleSavePodcastStateChanged: (Int, Boolean) -> Unit = { id, isSaved ->
                        Log.d("SaveStateChanged", "Podcast ID: $id is now saved: $isSaved")
                    }

                    val isNetworkAvailable = isNetworkAvailable(LocalContext.current)
                    LazyColumn {
                        if (isNetworkAvailable) {
                            if (ifContainsSavedPodcasts) {
                                item {
                                    HorizontalList(
                                        title = "Saved podcasts",
                                        listState = podcastsLazyListState,
                                        isLoading = false,
                                        items = podcastsListState,
                                        onLoadMore = { viewModel.loadSavedPodcasts() },
                                        navController = navController,
                                        routeToDetailedScreen = PodcastDetailedFeature.podcastScreen,
                                        showAddToSavedFragment = true,
                                        onSavePodcastStateChanged = handleSavePodcastStateChanged
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                            if (ifContainsLikedEpisodes) {
                                item {
                                    HorizontalList(
                                        title = "Liked episodes",
                                        listState = episodesLazyListState,
                                        isLoading = false,
                                        items = episodesListState,
                                        onLoadMore = { viewModel.loadLikedEpisodes() },
                                        navController = navController,
                                        routeToDetailedScreen = EpisodeDetailedFeature.episodeScreen,
                                        showAddToSavedFragment = false,
                                        onSavePodcastStateChanged = handleSavePodcastStateChanged
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }

                        if (ifContainsDownloadedEpisodes) {
                            item {
                                HorizontalList(
                                    title = "Downloaded episodes",
                                    listState = downloadedEpisodesLazyListState,
                                    isLoading = false,
                                    items = downloadedEpisodesListState,
                                    onLoadMore = { viewModel.loadDownloadedEpisodes() },
                                    navController = navController,
                                    routeToDetailedScreen = PlayerFeature.playerScreen,
                                    showAddToSavedFragment = false,
                                    onSavePodcastStateChanged = handleSavePodcastStateChanged
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                ErrorScreen()
            }
        }
    }
}
