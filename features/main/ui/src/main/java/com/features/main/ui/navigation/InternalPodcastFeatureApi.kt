package com.features.main.ui.navigation

import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.navigation_constants.MainFeature
import com.core.feature_api.FeatureApi

object InternalPodcastFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = MainFeature.mainScreenRoute,
            route = MainFeature.nestedRoute
        ) {
            composable(MainFeature.mainScreenRoute) {

            }
        }
    }

}