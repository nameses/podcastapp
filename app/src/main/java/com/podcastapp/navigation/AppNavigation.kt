package com.podcastapp.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import com.core.common.navigation_constants.AppStartPoint

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider
) {
    NavHost(navController = navController, startDestination = AppStartPoint.startPoint) {
        navigationProvider.podcastApi.registerGraph(
            navController, this
        )
        navigationProvider.authApi.registerGraph(
            navController, this
        )
    }
}