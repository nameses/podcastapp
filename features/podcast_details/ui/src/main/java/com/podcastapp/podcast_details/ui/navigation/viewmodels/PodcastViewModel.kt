package com.podcastapp.podcast_details.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import com.podcastapp.podcast_details.domain.use_cases.GetPodcastUseCase
import javax.inject.Inject

class PodcastViewModel @Inject constructor(
    private val podcastUseCase: GetPodcastUseCase
) : ViewModel() {

}