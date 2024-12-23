package com.podcastapp.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.podcastapp.commonui.viewmodels.HorizontalListItemViewModel

@Composable
fun AddToSavedFragment(
    id: Int,
    isInitiallySaved: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel: HorizontalListItemViewModel = hiltViewModel<HorizontalListItemViewModel>()

    LaunchedEffect(Unit) {
        viewModel.setInitialSavedState(isInitiallySaved)
    }

    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .size(60.dp)
            .padding(8.dp)
            .clickable {
                viewModel.toggleSaved(id)
            }
            .background(Color.White, shape = CircleShape)
    ) {
        if (state.data == true) {
            Icon(
                Icons.Filled.BookmarkAdded,
                contentDescription = null,
                tint = Color(0xFF6A1B9A),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Icon(
                Icons.Filled.BookmarkAdd,
                contentDescription = null,
                tint = Color(0xFF6A1B9A),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}