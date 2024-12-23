package com.podcastapp.profile.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.common.constants.AuthFeature
import com.core.common.constants.ProfileFeature
import com.core.feature_api.FeatureApi
import com.podcastapp.profile.domain.model.Podcast
import com.podcastapp.profile.ui.navigation.screen.EditProfileScreen
import com.podcastapp.profile.ui.navigation.screen.ProfileScreen
import com.podcastapp.profile.ui.navigation.viewmodels.EditProfileViewModel
import com.podcastapp.profile.ui.navigation.viewmodels.ProfileViewModel

object InternalProfileFeatureApi : FeatureApi {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = ProfileFeature.profileScreen,
            route = ProfileFeature.nestedRoute
        ) {
            composable(route = ProfileFeature.profileScreen) {
                val viewModel = hiltViewModel<ProfileViewModel>()
                ProfileScreen(
                    viewModel = viewModel,
                    onEditClick = { navController.navigate(ProfileFeature.profileEditScreen) },
                    onLogout = { navController.navigate(AuthFeature.nestedRoute) },
                    onPodcastClick = { navController.navigate("todo") }
                )
            }

            composable(route = ProfileFeature.profileEditScreen) {
                val viewModel = hiltViewModel<EditProfileViewModel>()
                EditProfileScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigate(ProfileFeature.profileScreen) }
                )
            }
        }
    }
}