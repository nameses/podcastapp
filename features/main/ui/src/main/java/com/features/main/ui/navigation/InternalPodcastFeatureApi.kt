package com.features.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.constants.MainFeature
import com.core.feature_api.FeatureApi
import androidx.compose.material3.Text

object InternalPodcastFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = MainFeature.mainScreenRoute,
            route = MainFeature.nestedRoute
        ) {
            composable(route=MainFeature.mainScreenRoute) {
                HelloWorldScreen()
            }
        }
    }
}
@Composable
fun HelloWorldScreen() {
    // Displaying "Hello World" text
    Text(text = "Hello World")
}