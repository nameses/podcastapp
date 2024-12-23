package com.features.main.ui.navigation.screen

import androidx.compose.runtime.Composable
import com.core.common.ui.HorizontalList
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel

@Composable
fun MainScreen(podcastFeaturedViewModel: PodcastFeaturedViewModel) {
    val items = viewModel.items
    val isLoading by viewModel.isLoading

    HorizontalList(
        items = items,
        isLoading = isLoading,
        onLoadMore = { viewModel.loadPodcasts() }
    )
}
