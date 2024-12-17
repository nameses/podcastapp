package com.podcastapp.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import com.core.common.navigation_constants.MainFeature

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider
) {
    NavHost(navController = navController, startDestination = MainFeature.nestedRoute) {
        navigationProvider.podcastApi.registerGraph(
            navController, this
        )
    }
}