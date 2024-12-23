package com.podcastapp.commonui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.core.common.model.RepoEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
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
                    isInitiallySaved = item.isInitiallySaved,
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
    isInitiallySaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HorizontalListItemViewModel = hiltViewModel<HorizontalListItemViewModel>()

    LaunchedEffect(Unit) {
        viewModel.setInitialSavedState(isInitiallySaved)
    }

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(8.dp)
            .background(Color.White, shape = CircleShape)
    ) {
        if (state.data == true) {
            // Purple strikethrough bookmark
            Icon(
                Icons.Filled.BookmarkAdded,
                contentDescription = null,
                tint = Color(0xFF6A1B9A), // Purple color
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Purple bookmark
            Icon(
                Icons.Filled.BookmarkAdd,
                contentDescription = null,
                tint = Color(0xFF6A1B9A), // Purple color
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@HiltViewModel
class HorizontalListItemViewModel @Inject constructor(private val podcastRepository: CommonPodcastRepository) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<Boolean>())
    val state: StateFlow<UiStateHolder<Boolean>> get() = _state

    fun setInitialSavedState(isInitiallySaved: Boolean) {
        if (_state.value.data == null) {
            _state.value = UiStateHolder(isSuccess = true, data = isInitiallySaved)
        }
    }

    fun toggleSaved(id: Int) = viewModelScope.launch {
        when (val response = podcastRepository.addToSaved(id)) {
            is RepoEvent.Success -> {
                _state.value = UiStateHolder(isSuccess = true, data = !_state.value.data!!)
            }
            is RepoEvent.Error -> {
                _state.value =
                    UiStateHolder(message = response.message.toString(), errors = response.errors)
            }
        }
    }
}

data class HorizontalListItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String?,
    val isInitiallySaved: Boolean
)
