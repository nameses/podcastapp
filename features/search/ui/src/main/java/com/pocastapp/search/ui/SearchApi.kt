package com.pocastapp.search.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi


interface SearchApi : FeatureApi
{

}

class SearchApiImpl : SearchApi
{
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalSearchFeatureApi.registerGraph(navController, navGraphBuilder)
    }

}