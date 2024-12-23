package com.core.common.ui

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.core.common.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Composable
fun HorizontalList(
    items: List<HorizontalListItem>,
    isLoading: Boolean = false,
    onLoadMore: () -> Unit = {},
    navController: NavHostController,
    routeToDetailedScreen: String,
    showAddToSavedFragment: Boolean
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { item ->
            HorizontalListItemView(
                item = item,
                onClick = { navController.navigate("${routeToDetailedScreen}/${item.id}") },
                showAddToSavedFragment = showAddToSavedFragment
            )
        }
        item {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size }
            .collect { visibleItems ->
                if (visibleItems >= items.size && !isLoading) {
                    onLoadMore()
                }
            }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HorizontalListItemView(
    item: HorizontalListItem,
    onClick: () -> Unit,
    showAddToSavedFragment: Boolean
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = item.imageUrl ?: R.drawable.default_item_list,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            if (showAddToSavedFragment) {
                HorizontalListItemFragment(
                    id = item.id,
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }

        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { onClick() }
        )

        Text(
            text = item.author,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun HorizontalListItemFragment(
    id: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HorizontalListItemViewModel = hiltViewModel<HorizontalListItemViewModel>()
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Text(
            text = "ID: $id",
            color = Color.White,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp)
        )
    }
}

@HiltViewModel
class HorizontalListItemViewModel @Inject constructor(podcastDataProvider: PodcastDataProviders) : ViewModel() {
    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> get() = _uiState

    init {
        // Any state management here
    }
}



data class HorizontalListItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String?
)
