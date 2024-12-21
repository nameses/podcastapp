package com.features.auth.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.navigation_constants.AuthFeature
import com.core.common.navigation_constants.MainFeature
import com.core.feature_api.FeatureApi
import com.features.auth.ui.navigation.screen.LoginScreen
import com.features.auth.ui.navigation.screen.RegisterScreen
import com.features.auth.ui.navigation.viewmodels.LoginViewModel
import com.features.auth.ui.navigation.viewmodels.RegisterViewModel

object InternalAuthFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = AuthFeature.loginScreen,
            route = AuthFeature.nestedRoute
        ) {
            composable(AuthFeature.loginScreen) {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onSignUpClick = { navController.navigate(AuthFeature.registerScreen) },
                    onSuccess = { navController.navigate(MainFeature.nestedRoute) })
            }
//        }
//        navGraphBuilder.navigation(
//            startDestination = AuthFeature.registerScreen,
//            route = AuthFeature.nestedRoute
//        ) {
            composable(AuthFeature.registerScreen) {
                val viewModel = hiltViewModel<RegisterViewModel>()
                RegisterScreen(
                    viewModel = viewModel,
                    onLoginClick = { navController.navigate(AuthFeature.loginScreen) },
                    onSuccess = { navController.navigate(MainFeature.nestedRoute) })
            }
        }
    }

}