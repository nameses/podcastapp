package com.features.main.ui.navigation.screen

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.core.common.constants.MainFeature
import com.core.common.constants.PodcastDetailedFeature
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel
import com.podcastapp.commonui.HorizontalList
import com.podcastapp.commonui.model.HorizontalListItem

@Composable
fun MainScreen(podcastFeaturedViewModel: PodcastFeaturedViewModel, navController: NavHostController) {
    val podcastFeaturedListState by podcastFeaturedViewModel.podcasts.collectAsState()
    val podcastFeaturedState by podcastFeaturedViewModel.state.collectAsState()
    val podcastFeaturedLazyListState = rememberLazyListState()

    HorizontalList(
        listState = podcastFeaturedLazyListState,
        isLoading = podcastFeaturedState.isLoading,
        items = podcastFeaturedListState,
        onLoadMore = { podcastFeaturedViewModel.loadPodcasts() },
        navController = navController,
        routeToDetailedScreen = PodcastDetailedFeature.podcastDetailedScreen,
        showAddToSavedFragment = true
    )
}
