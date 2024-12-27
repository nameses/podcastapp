package com.podcastapp.profile.ui.navigation.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.imageLoader
import com.podcastapp.profile.ui.navigation.viewmodels.ProfileViewModel
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.core.common.constants.PodcastDetailedFeature
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.podcastapp.commonui.HorizontalList
import com.podcastapp.commonui.errorscreen.ErrorScreen
import com.podcastapp.profile.ui.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    LaunchedEffect(state.data?.imageUrl) {
        context.imageLoader.memoryCache.clear()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.isSuccess -> {
                Column(modifier = Modifier.fillMaxSize()) {
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

                        Image(
                            painter = rememberImagePainter(
                                data = state.data?.imageUrl ?: R.drawable.default_avatar
                            ),
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
                                Text(text = "Edit",
                                    color = ColorWhite,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .clickable { onEditClick() }
                                        .align(Alignment.CenterStart)
                                        .padding(start = 8.dp))
                            }

                            Box(
                                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Profile",
                                    color = Color.White,
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
                                        .clickable { onLogout() }
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
                    val handleSavePodcastStateChanged: (Int, Boolean) -> Unit = { id, isSaved ->
                        Log.d("SaveStateChanged", "Podcast ID: $id is now saved: $isSaved")
                    }

                    LazyColumn {
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

                        item {
                            HorizontalList(
                                title = "Liked episodes",
                                listState = episodesLazyListState,
                                isLoading = false,
                                items = episodesListState,
                                onLoadMore = { viewModel.loadLikedEpisodes() },
                                navController = navController,
                                routeToDetailedScreen = "episode", //todo
                                showAddToSavedFragment = false,
                                onSavePodcastStateChanged = handleSavePodcastStateChanged
                            )
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
