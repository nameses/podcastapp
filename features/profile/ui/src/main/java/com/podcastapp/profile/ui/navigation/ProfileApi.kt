package com.podcastapp.profile.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface ProfileApi : FeatureApi {

}

class ProfileApiImpl : ProfileApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalProfileFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}