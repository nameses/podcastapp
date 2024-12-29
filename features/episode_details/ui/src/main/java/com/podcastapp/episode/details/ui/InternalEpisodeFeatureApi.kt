package com.podcastapp.episode.details.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.core.common.constants.EpisodeDetailedFeature
import com.core.feature_api.FeatureApi
import com.podcastapp.episode.details.ui.screen.EpisodeScreen
import com.podcastapp.episode.details.ui.viewmodels.EpisodeViewModel

object InternalEpisodeFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = "${EpisodeDetailedFeature.episodeScreen}/{id}",
            route = EpisodeDetailedFeature.nestedRoute
        ) {
            composable(
                route = "${EpisodeDetailedFeature.episodeScreen}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                    ?: throw IllegalArgumentException("Episode ID is required")
                val episodeViewModel = hiltViewModel<EpisodeViewModel>()
                EpisodeScreen(
                    navController = navController, viewModel = episodeViewModel, episodeId = id
                )
            }
        }
    }
}