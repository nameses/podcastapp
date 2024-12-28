package com.podcastapp.ui.navigation

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.core.common.constants.PlayerFeature
import com.core.feature_api.FeatureApi
import com.podcastapp.ui.navigation.screen.PlayerScreen
import com.podcastapp.ui.navigation.viewmodels.BasePlayerViewModel
import com.podcastapp.ui.navigation.viewmodels.PlayerViewModel
import javax.inject.Inject

object InternalPlayerFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = PlayerFeature.playerScreen,
            route = PlayerFeature.nestedRoute
        ) {
            composable(
                route = "${PlayerFeature.playerScreen}/{episode_id}",
                arguments = listOf(navArgument("episode_id") { type = NavType.IntType })
            ) { backStackEntry ->

                val id = backStackEntry.arguments?.getInt("episode_id")
                    ?: throw IllegalArgumentException("Podcast ID is required")

                PlayerScreen(episodeId = id, navController = navController)
            }

            composable(route = PlayerFeature.playerScreen, deepLinks = listOf(navDeepLink {
                uriPattern = PlayerFeature.playerScreenDeepLink
            })) {
                PlayerScreen(navController = navController)
            }
        }
    }
}

