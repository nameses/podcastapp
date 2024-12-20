package com.podcastapp.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import com.core.common.navigation_constants.AppStartPoint
import com.core.common.navigation_constants.AuthFeature
import com.core.common.navigation_constants.MainFeature
import com.core.common.services.TokenManager
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider,
    tokenManager: TokenManager
) {
    val isAuthenticated = runBlocking { tokenManager.containsJwtToken() }
    val startDestination = if (isAuthenticated) MainFeature.nestedRoute else AuthFeature.nestedRoute

    NavHost(navController = navController, startDestination = startDestination) {
        navigationProvider.podcastApi.registerGraph(
            navController, this
        )
        navigationProvider.authApi.registerGraph(
            navController, this
        )
    }
}