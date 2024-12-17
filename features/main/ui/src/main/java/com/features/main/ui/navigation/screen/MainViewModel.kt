package com.features.main.ui.navigation.screen

import androidx.lifecycle.ViewModel
import com.features.main.domain.use_cases.GetPopularPodcastListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getPodcastListUseCase: GetPopularPodcastListUseCase) :
    ViewModel() {
    //todo
}