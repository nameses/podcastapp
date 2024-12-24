package com.podcastapp.commonui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorizontalListItemViewModel @Inject constructor(private val podcastRepository: CommonPodcastRepository) :
    ViewModel() {

    private val _savedState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val savedState: StateFlow<Map<Int, Boolean>> get() = _savedState

    fun loadSavedState(mapSavedPodcasts: Map<Int, Boolean>) {
        _savedState.value = mapSavedPodcasts
    }

    fun toggleSaved(id: Int) = viewModelScope.launch {
        podcastRepository.addToSaved(id)
    }

    fun setNewStatus(id: Int) = viewModelScope.launch {
        val statusById = podcastRepository.GetPodcast();

        val currentState = _savedState.value
        val newState = currentState.toMutableMap()
        val isCurrentlySaved = currentState[id] ?: false
        newState[id] = !isCurrentlySaved
        _savedState.value = newState
    }
}
