package com.podcastapp.ui.navigation.viewmodels

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavHostController
import com.core.common.constants.PlayerFeature
import com.core.common.constants.ProfileFeature
import com.core.common.model.RepoEvent
import com.core.common.model.UiStateHolder
import com.doublesymmetry.kotlinaudio.models.AudioItem
import com.doublesymmetry.kotlinaudio.models.DefaultAudioItem
import com.doublesymmetry.kotlinaudio.models.NotificationButton
import com.doublesymmetry.kotlinaudio.models.NotificationConfig
import com.doublesymmetry.kotlinaudio.models.PlayerConfig
import com.doublesymmetry.kotlinaudio.models.RepeatMode
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import com.podcastapp.commonrepos.repos.CommonEpisodeRepository
import com.podcastapp.ui.navigation.mapper.toAudioItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import com.doublesymmetry.kotlinaudio.models.MediaSessionCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val basePlayer: BasePlayerViewModel,
    private val commonEpisodeRepository: CommonEpisodeRepository,
    @ApplicationContext private val appContext: Context,
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

//    private val _state = MutableStateFlow(
//        QueuedAudioPlayer(
//            context = appContext, playerConfig = PlayerConfig(
//                interceptPlayerActionsTriggeredExternally = true,
//                handleAudioBecomingNoisy = true,
//                handleAudioFocus = true
//            )
//        )
//    )
//    val state: StateFlow<QueuedAudioPlayer> get() = _state


    fun observePlayer() {
        basePlayer._state.value.event.audioItemTransition.onEach {
            basePlayer._title.value = basePlayer._state.value.currentItem?.title ?: ""
            basePlayer._artist.value = basePlayer._state.value.currentItem?.artist ?: ""
            basePlayer._artwork.value = basePlayer._state.value.currentItem?.artwork ?: ""
            basePlayer._duration.value = basePlayer._state.value.currentItem?.duration ?: 0
            basePlayer._isLive.value = basePlayer._state.value.isCurrentMediaItemLive == true

            val episodeId = basePlayer._state.value.currentItem?.albumTitle?.toInt()
            val isLiked = commonEpisodeRepository.getEpisode(episodeId ?: 0).data?.data?.is_liked ?: false
            basePlayer._isLiked.value = isLiked
        }.launchIn(viewModelScope)

        basePlayer._state.value.event.stateChange.onEach {
            basePlayer._isPlaying.value = basePlayer._state.value.isPlaying == true
        }.launchIn(viewModelScope)

        basePlayer._state.value.event.onPlayerActionTriggeredExternally.onEach {
            when (it) {
                MediaSessionCallback.PLAY -> basePlayer._state.value.play()
                MediaSessionCallback.PAUSE -> basePlayer._state.value.pause()
                MediaSessionCallback.NEXT -> basePlayer._state.value.next()
                MediaSessionCallback.PREVIOUS -> basePlayer._state.value.previous()
                MediaSessionCallback.STOP -> basePlayer._state.value.stop()
                is MediaSessionCallback.SEEK -> basePlayer._state.value.seek(
                    it.positionMs, TimeUnit.MILLISECONDS
                )

                else -> Timber.d("Event not handled")
            }
        }.launchIn(viewModelScope)
    }

    fun playEpisode(episodeId: Int) = viewModelScope.launch {
        var episode = commonEpisodeRepository.getEpisode(episodeId);
        if (episode is RepoEvent.Error) {
            //todo
            Log.d("EPISODE", "Error while episode get: ${episode.message}")
            return@launch;
        }
        if (episode.data == null) {
            //todo
            Log.d("EPISODE", "Episode data not found")
            return@launch;
        }

        basePlayer._state.value.clear()//todo don't know if its right method
        basePlayer._state.value.add(episode.data!!.data.toAudioItem())
        basePlayer._state.value.play()

        //add to queue all in episode.next_episodes
        val nextEpisodes =
            episode.data!!.data.next_episodes.map { it.toAudioItem(episode.data!!.data.podcast.author.name) }
        basePlayer._state.value.add(nextEpisodes)
    }

    init {
        basePlayer._state.value.playerOptions.repeatMode = RepeatMode.ALL

        setupNotification()
    }

//    fun startSavingEpisodeDataEverySecond() {
//        viewModelScope.launch {
//            while (true) {
//                delay(800)
//                savedStateHandle.set("player_state", basePlayer.state.value)
//            }
//        }
//    }

    private fun setupNotification() {
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
                appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
        )

        basePlayer._state.value.notificationManager.createNotification(notificationConfig)
    }

    fun pausePlayback() {
        basePlayer._state.value.pause()
    }

    fun likeEpisode() = viewModelScope.launch {
        val episodeId = basePlayer._state.value.currentItem?.albumTitle?.toInt() ?: 0
        var response = commonEpisodeRepository.likeEpisode(episodeId)
    }

//    fun retrieveLastPlayedEpisode() {
//        val sharedPreferences = appContext.getSharedPreferences("LastPlayedEpisode", Context.MODE_PRIVATE)
//
//        _title.value = sharedPreferences.getString("title", "") ?: ""
//        _artist.value = sharedPreferences.getString("artist", "") ?: ""
//        _artwork.value = sharedPreferences.getString("artwork", "") ?: ""
//        _duration.value = sharedPreferences.getLong("duration", 0L)
//        _isLive.value = sharedPreferences.getBoolean("isLive", false)
//    }
//
//    fun startSavingEpisodeDataEverySecond() {
//        viewModelScope.launch {
//            while (true) {
//                delay(800)
//                saveCurrentEpisodeData()
//            }
//        }
//    }
//
//    private fun saveCurrentEpisodeData() {
//        val sharedPreferences = appContext.getSharedPreferences("LastPlayedEpisode", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        val currentItem = _state.value.currentItem
//
//        if (currentItem != null) {
//            editor.putString("title", currentItem.title)
//            editor.putString("artist", currentItem.artist)
//            editor.putString("artwork", currentItem.artwork)
//            editor.putLong("duration", currentItem.duration!!)
//            editor.putBoolean("isLive", _isLive.value)
//            editor.apply()
//        }
//    }
}