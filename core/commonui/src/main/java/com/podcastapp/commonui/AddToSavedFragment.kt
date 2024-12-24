package com.podcastapp.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.sharp.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.podcastapp.commonui.viewmodels.HorizontalListItemViewModel

@Composable
fun AddToSavedFragment(
    id: Int,
    viewModel: HorizontalListItemViewModel,
    isInitiallySaved: Boolean,
    modifier: Modifier = Modifier
) {
    var isSaved by remember { mutableStateOf(isInitiallySaved) }

    Box(
        modifier = modifier
            .size(40.dp)
            .padding(start = 8.dp, bottom = 8.dp)
            .background(color = Color.White, shape = CircleShape)
            .clickable {
                viewModel.toggleSaved(id);
                isSaved = viewModel.setNewStatus(id);
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = if (isSaved) "Saved" else "Not Saved",
            tint = Color(0xFF7971EA),
            modifier = Modifier.size(20.dp)
        )
    }
}
