package com.features.main.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface MainApi : FeatureApi {

}

class MainApiImpl : MainApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMainFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}