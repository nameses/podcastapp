package com.podcastapp.ui.navigation.viewmodels

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import com.doublesymmetry.kotlinaudio.models.PlayerConfig
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BasePlayerViewModel(
    private val context: Context, private val commonEpisodeRepository: CommonEpisodeRepository
) : ViewModel() {

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

    fun observePlayer() {
        _state.value.event.audioItemTransition.onEach {
            _title.value = _state.value.currentItem?.title ?: ""
            _artist.value = _state.value.currentItem?.artist ?: ""
            _artwork.value = _state.value.currentItem?.artwork ?: ""
            _duration.value = _state.value.currentItem?.duration ?: 0
            _isLive.value = _state.value.isCurrentMediaItemLive == true

            val episodeId = _state.value.currentItem?.albumTitle?.toInt()
            val isLiked =
                commonEpisodeRepository.getEpisode(episodeId ?: 0).data?.data?.is_liked ?: false
            _isLiked.value = isLiked
        }.launchIn(viewModelScope)

        _state.value.event.stateChange.onEach {
            _isPlaying.value = _state.value.isPlaying == true
        }.launchIn(viewModelScope)

        _state.value.event.onPlayerActionTriggeredExternally.onEach {
            when (it) {
                MediaSessionCallback.PLAY -> _state.value.play()
                MediaSessionCallback.PAUSE -> _state.value.pause()
                MediaSessionCallback.NEXT -> _state.value.next()
                MediaSessionCallback.PREVIOUS -> _state.value.previous()
                MediaSessionCallback.STOP -> _state.value.stop()
                is MediaSessionCallback.SEEK -> _state.value.seek(
                    it.positionMs, TimeUnit.MILLISECONDS
                )

                else -> Timber.d("Event not handled")
            }
        }.launchIn(viewModelScope)
    }

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