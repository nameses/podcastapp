package com.features.main.ui.navigation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.core.common.constants.PodcastDetailedFeature
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel
import com.features.main.ui.navigation.viewmodels.PodcastNewViewModel
import com.features.main.ui.navigation.viewmodels.PodcastPopularViewModel
import com.podcastapp.commonui.HorizontalList

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavHostController,
    podcastFeaturedViewModel: PodcastFeaturedViewModel,
    podcastPopularViewModel: PodcastPopularViewModel,
    podcastNewViewModel: PodcastNewViewModel,
) {
    val podcastFeaturedListState by podcastFeaturedViewModel.podcasts.collectAsState()
    val podcastFeaturedLazyListState = rememberLazyListState()

    val podcastPopularListState by podcastPopularViewModel.podcasts.collectAsState()
    val podcastPopularLazyListState = rememberLazyListState()

    val podcastNewListState by podcastNewViewModel.podcasts.collectAsState()
    val podcastNewLazyListState = rememberLazyListState()

    val handleSavePodcastStateChanged: (Int, Boolean) -> Unit = { id, isSaved ->
        Log.d("SaveStateChanged", "Podcast ID: $id is now saved: $isSaved")
    }

    LazyColumn {
        item {
            HorizontalList(
                title = "Featured",
                listState = podcastFeaturedLazyListState,
                isLoading = podcastFeaturedViewModel.podcasts.value.isLoading,
                items = podcastFeaturedListState.data ?: emptyList(),
                onLoadMore = { podcastFeaturedViewModel.loadPodcasts() },
                navController = navController,
                routeToDetailedScreen = PodcastDetailedFeature.podcastScreen,
                showAddToSavedFragment = true,
                onSavePodcastStateChanged = handleSavePodcastStateChanged
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            HorizontalList(
                title = "Popular",
                listState = podcastPopularLazyListState,
                isLoading = podcastPopularViewModel.podcasts.value.isLoading,
                items = podcastPopularListState.data ?: emptyList(),
                onLoadMore = { podcastPopularViewModel.loadPodcasts() },
                navController = navController,
                routeToDetailedScreen = PodcastDetailedFeature.podcastScreen,
                showAddToSavedFragment = true,
                onSavePodcastStateChanged = handleSavePodcastStateChanged
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            HorizontalList(
                title = "Last added",
                listState = podcastNewLazyListState,
                isLoading = podcastNewViewModel.podcasts.value.isLoading,
                items = podcastNewListState.data ?: emptyList(),
                onLoadMore = { podcastNewViewModel.loadPodcasts() },
                navController = navController,
                routeToDetailedScreen = PodcastDetailedFeature.podcastScreen,
                showAddToSavedFragment = true,
                onSavePodcastStateChanged = handleSavePodcastStateChanged
            )
        }
    }
}
