package com.podcastapp.podcast_details.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.core.feature_api.FeatureApi

interface PodcastApi : FeatureApi {

}

class PodcastApiImpl : PodcastApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalPodcastFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}