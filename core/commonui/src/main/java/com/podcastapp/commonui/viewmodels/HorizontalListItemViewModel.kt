package com.podcastapp.commonui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podcastapp.commonrepos.repos.CommonPodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorizontalListItemViewModel @Inject constructor(private val podcastRepository: CommonPodcastRepository) :
    ViewModel() {

    private val _savedState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val savedState: StateFlow<Map<Int, Boolean>> get() = _savedState

    private val _onSaveStateChanged = MutableSharedFlow<Pair<Int, Boolean>>()
    val onSaveStateChanged: SharedFlow<Pair<Int, Boolean>> get() = _onSaveStateChanged

    fun loadSavedState(mapSavedPodcasts: Map<Int, Boolean>) {
        _savedState.value = mapSavedPodcasts
    }

    fun toggleSaved(id: Int, newValue: Boolean) = viewModelScope.launch {
        podcastRepository.addToSaved(id)
        setNewStatus(id, newValue)
        _onSaveStateChanged.emit(id to newValue)
    }

    fun setNewStatus(id: Int, newValue: Boolean) = viewModelScope.launch {
        val currentState = _savedState.value
        val newState = currentState.toMutableMap()
        newState[id] = newValue
        _savedState.value = newState
    }

    fun getSavedState(id: Int): Boolean {
        return _savedState.value[id] ?: false;
    }

    fun getSavedStateNullable(id: Int):Boolean?{
        return _savedState.value[id]
    }
}
