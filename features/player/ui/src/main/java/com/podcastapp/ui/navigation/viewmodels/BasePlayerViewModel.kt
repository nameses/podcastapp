package com.podcastapp.ui.navigation.viewmodels

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.constants.PlayerFeature
import com.core.common.model.UiEvent
import com.core.common.services.isNetworkAvailable
import com.core.network.model.episodes.EpisodeDTO
import com.doublesymmetry.kotlinaudio.models.AudioItem
import com.doublesymmetry.kotlinaudio.models.AudioItemTransitionReason
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import com.doublesymmetry.kotlinaudio.models.NotificationButton
import com.doublesymmetry.kotlinaudio.models.NotificationConfig
import com.doublesymmetry.kotlinaudio.models.PlayerConfig
import com.doublesymmetry.kotlinaudio.models.RepeatMode
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.domain.use_cases.GetEpisodeUseCase
import com.podcastapp.ui.navigation.mapper.getMp3DurationInSeconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BasePlayerViewModel(
    private val context: Context,
    private val commonEpisodeRepository: CommonEpisodeRepository,
    private val getEpisodeUseCase: GetEpisodeUseCase,
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

    val _audioUrl = MutableStateFlow("")
    val audioUrl: StateFlow<String> = _audioUrl

    val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id

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

    val _nextEpisodesItems = MutableStateFlow<List<EpisodeDTO>>(emptyList())
    val nextEpisodesItems: StateFlow<List<EpisodeDTO>> = _nextEpisodesItems

    init {
        _state.value.playerOptions.repeatMode = RepeatMode.OFF

        setupNotification()
        observePlayer()
    }

    private var lastLoadTime = 0L
    fun loadNextEpisodes() = viewModelScope.launch {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastLoadTime < 2000) {
            Timber.tag("Next episode event").d("Skipped loadNextEpisodes, triggered too soon")
            return@launch
        }

        lastLoadTime = currentTime

        val episodeId = _state.value.currentItem?.albumTitle?.toInt() ?: 0
        getEpisodeUseCase(episodeId).collect {
            when (it) {
                is UiEvent.Success -> {
                    if (it.data?.next_episodes?.isNotEmpty() == true) {
                        it.data?.next_episodes?.let { episodes ->
                            episodes.forEach { nit ->
                                nit.duration = getMp3DurationInSeconds(nit.file_path).toInt()
                                if (nit.duration / 60 > 0) {
                                    nit.duration = (nit.duration / 60)
                                } else {
                                    nit.duration = 1
                                }
                            }
                        }
                        _nextEpisodesItems.value = it.data?.next_episodes!!
                    } else {
                        _nextEpisodesItems.value = emptyList()
                    }
                }

                else -> {}
            }
        }
    }

    private fun setupNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(PlayerFeature.playerScreenDeepLink)
            }

            val notificationConfig = NotificationConfig(
                listOf(
                    NotificationButton.PLAY_PAUSE(),
                    NotificationButton.NEXT(isCompact = true),
                    NotificationButton.PREVIOUS(isCompact = true),
                    NotificationButton.SEEK_TO
                ), accentColor = null, smallIcon = null, pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_IMMUTABLE
                )
            )
            withContext(Dispatchers.Main) {
                _state.value.notificationManager.createNotification(notificationConfig)
            }
        }
    }

    fun observePlayer() {
        _state.value.event.audioItemTransition.onEach {
            _audioUrl.value = _state.value.currentItem?.audioUrl ?: ""
            _id.value = _state.value.currentItem?.albumTitle ?: ""
            _title.value = _state.value.currentItem?.title ?: ""
            _artist.value = _state.value.currentItem?.artist ?: ""
            _artwork.value = _state.value.currentItem?.artwork ?: ""
            _duration.value = _state.value.currentItem?.duration ?: 0
            _isLive.value = _state.value.isCurrentMediaItemLive == true

            val episodeId = _state.value.currentItem?.albumTitle?.toInt()
            if(isNetworkAvailable(context)) {
                val isLiked =
                    commonEpisodeRepository.getEpisode(episodeId ?: 0).data?.data?.is_liked ?: false
                _isLiked.value = isLiked

                CoroutineScope(Dispatchers.IO).launch {
                    loadNextEpisodes()
                }
            }
        }.launchIn(viewModelScope)

        _state.value.event.stateChange.onEach {
            _isPlaying.value = _state.value.isPlaying == true
//            loadNextEpisodes()
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