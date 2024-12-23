package com.podcastapp.profile.ui.navigation.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.podcastapp.profile.ui.navigation.viewmodels.ProfileViewModel
import coil.compose.rememberImagePainter
import com.core.common.theme.Purple500
import com.podcastapp.profile.ui.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit,
    onLogout: () -> Unit,
    onPodcastClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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
                            .background(Purple500)
                            .height(200.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = state.data?.imageUrl ?: R.drawable.default_avatar
                            ),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color.White, CircleShape)
                                .align(Alignment.Center)
                        )

                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                        }

                        Text(
                            text = "Profile",
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = state.data?.username ?: "",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

//                    HorizontalScrollableList(
//                        title = "Saved",
//                        items = state.data?.savedPodcasts.orEmpty().toHorizontalListItem(),
//                        onItemClick = onPodcastClick
//                    )

//                    HorizontalScrollableList(
//                        title = "Downloaded",
//                        items = state.data?.liked_episodes.orEmpty()
//                    )
                }
            }

            else -> {
                Text(
                    text = "Error: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
