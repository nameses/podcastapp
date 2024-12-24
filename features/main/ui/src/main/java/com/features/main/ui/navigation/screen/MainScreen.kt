package com.features.main.ui.navigation.screen

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
import com.features.main.ui.navigation.viewmodels.PodcastPopularViewModel
import com.podcastapp.commonui.HorizontalList

@Composable
fun MainScreen(
    navController: NavHostController,
    podcastFeaturedViewModel: PodcastFeaturedViewModel,
    podcastPopularViewModel: PodcastPopularViewModel
) {
    val podcastFeaturedListState by podcastFeaturedViewModel.podcasts.collectAsState()
    val podcastFeaturedState by podcastFeaturedViewModel.state.collectAsState()
    val podcastFeaturedLazyListState = rememberLazyListState()

    val podcastPopularListState by podcastPopularViewModel.podcasts.collectAsState()
    val podcastPopularState by podcastPopularViewModel.state.collectAsState()
    val podcastPopularLazyListState = rememberLazyListState()

    val handleSavePodcastStateChanged: (Int, Boolean) -> Unit = { id, isSaved ->
        Log.d("SaveStateChanged", "Podcast ID: $id is now saved: $isSaved")
    }

    LazyColumn {
        item {
            HorizontalList(
                title = "Featured",
                listState = podcastFeaturedLazyListState,
                isLoading = podcastFeaturedState.isLoading,
                items = podcastFeaturedListState,
                onLoadMore = { podcastFeaturedViewModel.loadPodcasts() },
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
                title = "Popular",
                listState = podcastPopularLazyListState,
                isLoading = podcastPopularState.isLoading,
                items = podcastPopularListState,
                onLoadMore = { podcastPopularViewModel.loadPodcasts() },
                navController = navController,
                routeToDetailedScreen = PodcastDetailedFeature.podcastScreen,
                showAddToSavedFragment = true,
                onSavePodcastStateChanged = handleSavePodcastStateChanged
            )
        }
    }
}
