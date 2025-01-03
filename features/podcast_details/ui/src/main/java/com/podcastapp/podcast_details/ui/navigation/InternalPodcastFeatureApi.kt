package com.podcastapp.podcast_details.ui.navigation

import androidx.navigation.navigation
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
            startDestination = "${PodcastDetailedFeature.podcastScreen}/{id}",
            route = PodcastDetailedFeature.nestedRoute
        ) {
            composable(
                route = "${PodcastDetailedFeature.podcastScreen}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                    ?: throw IllegalArgumentException("Podcast ID is required")
                val podcastViewModel = hiltViewModel<PodcastViewModel>()
                PodcastScreen(
                    navController = navController, viewModel = podcastViewModel, podcastId = id
                )
            }
        }
    }
}