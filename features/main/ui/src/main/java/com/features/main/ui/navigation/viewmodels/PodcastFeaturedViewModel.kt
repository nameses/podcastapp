package com.features.main.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.features.main.domain.model.PodcastDTO
import com.features.main.domain.model.PodcastList
import com.features.main.domain.use_cases.GetFeaturedPodcastListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastFeaturedViewModel @Inject constructor(
    private val podcastUseCase: GetFeaturedPodcastListUseCase
) : ViewModel() {
    private val _podcasts = MutableStateFlow<List<PodcastDTO>>(emptyList())
    val podcasts: MutableStateFlow<List<PodcastDTO>> get() = _podcasts
    private var currentPage = 1

    private val _loadState = MutableStateFlow(UiStateHolder<PodcastList>())
    val loadState: StateFlow<UiStateHolder<PodcastList>> get() = _loadState

    init {
        loadPodcasts()
    }

    fun loadPodcasts() = viewModelScope.launch {

        podcastUseCase(currentPage).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _loadState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    if (it.data?.items?.isNotEmpty() == true) {
                        _podcasts.value += it.data!!.items
                        currentPage++
                    }
                    _loadState.value = UiStateHolder(isSuccess = true, data = it.data)
                }

                is UiEvent.Error -> {
                    _loadState.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }

    }
}