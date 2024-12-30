package com.pocastapp.search.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.core.common.constants.SearchFeature
import androidx.navigation.navigation
import com.core.feature_api.FeatureApi
import com.pocastapp.search.ui.screen.SearchScreen
import com.pocastapp.search.ui.viewmodels.SearchViewModule


object InternalSearchFeatureApi : FeatureApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = SearchFeature.searchScreen,
            route = SearchFeature.nestedRoute
        ) {
            composable(
                route = SearchFeature.searchScreen
            ) {
                val viewModel: SearchViewModule = hiltViewModel()
                SearchScreen(viewModel,navController)
            }
        }
    }
}