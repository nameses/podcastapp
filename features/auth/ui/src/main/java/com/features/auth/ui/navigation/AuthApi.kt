package com.features.auth.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface AuthApi : FeatureApi {

}

class AuthApiImpl : AuthApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalAuthFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}