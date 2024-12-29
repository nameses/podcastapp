package com.podcastapp.commonui

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.core.common.constants.PlayerFeature
import com.podcastapp.commonui.model.HorizontalListItem
import com.podcastapp.commonui.viewmodels.HorizontalListItemViewModel

@Composable
fun HorizontalList(
    title: String,
    listState: LazyListState,
    items: List<HorizontalListItem>,
    isLoading: Boolean = false,
    onLoadMore: () -> Unit = {},
    navController: NavHostController,
    routeToDetailedScreen: String,
    showAddToSavedFragment: Boolean,
    onSavePodcastStateChanged: (Int, Boolean) -> Unit
) {
    val horizontalListItemViewModel = hiltViewModel<HorizontalListItemViewModel>()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
        )

        LazyRow(
            state = listState,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items) { item ->
                HorizontalListItem(
                    item = item,
                    viewModel = horizontalListItemViewModel,
                    onClick = {
                        navController.navigate("${routeToDetailedScreen}/${item.id}")
                    },
                    showAddToSavedFragment = showAddToSavedFragment,
                    onSaveStateChanged = onSavePodcastStateChanged
                )
            }
            item {
                if (isLoading) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size }.collect { visibleItems ->
                if (visibleItems >= items.size && !isLoading) {
                    onLoadMore()
                }
            }
        }
    }
}