package com.podcastapp.commonui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.RepoEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorizontalListItemViewModel @Inject constructor(private val podcastRepository: CommonPodcastRepository) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<Boolean>())
    val state: StateFlow<UiStateHolder<Boolean>> get() = _state

    fun setInitialSavedState(isInitiallySaved: Boolean) {
        if (_state.value.data == null) {
            _state.value = UiStateHolder(isSuccess = true, data = isInitiallySaved)
        }
    }

    fun toggleSaved(id: Int) = viewModelScope.launch {
        when (val response = podcastRepository.addToSaved(id)) {
            is RepoEvent.Success -> {
                _state.value = UiStateHolder(isSuccess = true, data = !_state.value.data!!)
            }
            is RepoEvent.Error -> {
                _state.value =
                    UiStateHolder(message = response.message.toString(), errors = response.errors)
            }
        }
    }
}