package com.podcastapp.podcast_details.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.podcast_details.domain.model.Podcast
import com.podcastapp.podcast_details.domain.use_cases.GetPodcastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastViewModel @Inject constructor(
    private val podcastUseCase: GetPodcastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiStateHolder<Podcast>())
    val state: StateFlow<UiStateHolder<Podcast>> get() = _state

    fun toggleSaveStatus(id: Int) = viewModelScope.launch { podcastUseCase.toggleSaveStatus(id) }

    fun loadPodcast(id: Int) = viewModelScope.launch {
        podcastUseCase(id).collect {
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    if (it.data == null) return@collect;
                    _state.value = UiStateHolder(isSuccess = true, data = it.data)
                }

                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }

}