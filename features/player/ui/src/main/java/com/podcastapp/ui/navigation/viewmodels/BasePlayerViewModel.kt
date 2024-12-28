package com.podcastapp.ui.navigation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.doublesymmetry.kotlinaudio.models.PlayerConfig
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BasePlayerViewModel(private val context: Context) : ViewModel() {

    val _state = MutableStateFlow(
        QueuedAudioPlayer(
            context = context, playerConfig = PlayerConfig(
                interceptPlayerActionsTriggeredExternally = true,
                handleAudioBecomingNoisy = true,
                handleAudioFocus = true
            )
        )
    )
    val state: StateFlow<QueuedAudioPlayer> get() = _state

    val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    val _artist = MutableStateFlow("")
    val artist: StateFlow<String> = _artist

    val _artwork = MutableStateFlow("")
    val artwork: StateFlow<String> = _artwork

    val _duration = MutableStateFlow<Long>(0)
    val duration: StateFlow<Long> = _duration

    val _isLive = MutableStateFlow(false)
    val isLive: StateFlow<Boolean> = _isLive

    val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked

    val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun togglePlayStopButton() {
        if (_state.value.isPlaying) {
            _state.value.stop()
        } else {
            _state.value.play()
        }
    }

    fun ifContainsEpisode(): Boolean {
        return _state.value.items.any()
    }
}