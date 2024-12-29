package com.podcastapp.episode.details.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.episode.details.domain.use_cases.GetEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.podcastapp.episode.details.domain.models.Episode

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val podcastUseCase: GetEpisodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiStateHolder<Episode>())
    val state: StateFlow<UiStateHolder<Episode>> get() = _state

    fun toggleLikeStatus(id: Int) = viewModelScope.launch { podcastUseCase.toggleLikeStatus(id) }

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