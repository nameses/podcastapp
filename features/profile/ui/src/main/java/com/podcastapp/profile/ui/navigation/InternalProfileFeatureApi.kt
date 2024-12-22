package com.podcastapp.profile.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.constants.ProfileFeature
import com.core.feature_api.FeatureApi

object InternalProfileFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = ProfileFeature.profileScreen,
            route = ProfileFeature.nestedRoute
        ) {
            composable(ProfileFeature.profileScreen) {
//                val viewModel = hiltViewModel<LoginViewModel>()
//                LoginScreen(
//                    viewModel = viewModel,
//                    onSignUpClick = { navController.navigate(AuthFeature.registerScreen) },
//                    onSuccess = { navController.navigate(MainFeature.nestedRoute) })
            }
        }
    }
}