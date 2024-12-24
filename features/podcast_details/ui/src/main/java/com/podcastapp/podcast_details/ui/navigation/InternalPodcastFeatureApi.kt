package com.podcastapp.podcast_details.ui.navigation

import androidx.navigation.navigation
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.constants.MainFeature
import com.core.common.constants.PodcastDetailedFeature
import com.core.feature_api.FeatureApi
import com.podcastapp.podcast_details.ui.navigation.screen.PodcastScreen
import com.podcastapp.podcast_details.ui.navigation.viewmodels.PodcastViewModel

object InternalPodcastFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = PodcastDetailedFeature.podcastScreen,
            route = PodcastDetailedFeature.nestedRoute
        ) {
            composable(route = MainFeature.mainScreenRoute) {
                val podcastViewModel = hiltViewModel<PodcastViewModel>()
                PodcastScreen(
                    podcastViewModel = podcastViewModel
                )
            }
        }
    }
}