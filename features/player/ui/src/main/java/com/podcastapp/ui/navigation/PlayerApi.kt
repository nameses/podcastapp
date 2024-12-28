package com.podcastapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface PlayerApi : FeatureApi {

}

class PlayerApiImpl : PlayerApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalPlayerFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}