package com.features.main.ui.navigation


import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.constants.MainFeature
import com.core.feature_api.FeatureApi
import com.features.main.ui.navigation.screen.PodcastListScreen
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel

object InternalPodcastFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = MainFeature.mainScreenRoute,
            route = MainFeature.nestedRoute
        ) {
            composable(route = MainFeature.mainScreenRoute) {
                val viewModel = hiltViewModel<PodcastFeaturedViewModel>()
                PodcastListScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}

