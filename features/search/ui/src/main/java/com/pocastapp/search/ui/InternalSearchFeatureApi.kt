package com.pocastapp.search.ui
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.core.common.constants.SearchFeature
import androidx.navigation.navigation
import com.core.feature_api.FeatureApi


object InternalSearchFeatureApi : FeatureApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = SearchFeature.searchScreen,
            route = SearchFeature.nestedRoute
        ){
            composable(
                route = SearchFeature.searchScreen
            ) {

            }
        }
    }
}