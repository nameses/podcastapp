package com.podcastapp.episode.details.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface EpisodeApi : FeatureApi {

}

class EpisodeApiImpl : EpisodeApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalEpisodeFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}