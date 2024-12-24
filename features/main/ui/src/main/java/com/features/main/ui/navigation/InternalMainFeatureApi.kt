package com.features.main.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.constants.MainFeature
import com.core.feature_api.FeatureApi
import com.features.main.ui.navigation.screen.MainScreen
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel
import com.features.main.ui.navigation.viewmodels.PodcastPopularViewModel

object InternalMainFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = MainFeature.mainScreenRoute,
            route = MainFeature.nestedRoute
        ) {
            composable(route = MainFeature.mainScreenRoute) {
                val podcastFeaturedViewModel = hiltViewModel<PodcastFeaturedViewModel>()
                val podcastPopularViewModel = hiltViewModel<PodcastPopularViewModel>()
                MainScreen(
                    navController = navController,
                    podcastFeaturedViewModel = podcastFeaturedViewModel,
                    podcastPopularViewModel = podcastPopularViewModel,
                )
            }
        }
    }
}

