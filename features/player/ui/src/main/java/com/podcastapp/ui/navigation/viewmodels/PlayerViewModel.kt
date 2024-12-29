package com.podcastapp.ui.navigation.viewmodels

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore.Audio
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
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.core.network.model.episodes.EpisodeDTO
import com.core.network.model.episodes.EpisodeFullDTO
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
import com.podcastapp.domain.use_cases.GetEpisodeUseCase
import com.podcastapp.ui.navigation.mapper.getMp3DurationInSeconds
import com.podcastapp.ui.navigation.model.Episode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val basePlayer: BasePlayerViewModel,
    private val commonEpisodeRepository: CommonEpisodeRepository,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    @ApplicationContext private val appContext: Context,
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _loadState = MutableStateFlow(UiStateHolder<EpisodeFullDTO>())
    val loadState: StateFlow<UiStateHolder<EpisodeFullDTO>> get() = _loadState

//    fun loadNextEpisodes() = viewModelScope.launch {
//        val episodeId = basePlayer._state.value.currentItem?.albumTitle?.toInt() ?: 0
//        getEpisodeUseCase(episodeId).collect {
//            when (it) {
//                is UiEvent.Loading -> {
//                    _loadState.value = UiStateHolder(isLoading = true)
//                }
//
//                is UiEvent.Success -> {
//                    if (it.data?.next_episodes?.isNotEmpty() == true) {
//                        it.data?.next_episodes!!.forEach { nit ->
//                            nit.duration = getMp3DurationInSeconds(nit.file_path).toInt()
//                        }
//                        nextEpisodesItems.value = it.data?.next_episodes!!
//                    }
//                    _loadState.value = UiStateHolder(isSuccess = true, data = it.data)
//                }
//
//                is UiEvent.Error -> {
//                    _loadState.value =
//                        UiStateHolder(message = it.message.toString(), errors = it.errors)
//                }
//            }
//        }
//    }

    fun playEpisode(episodeId: Int) = viewModelScope.launch {
        getEpisodeUseCase(episodeId).collect {
            when (it) {
                is UiEvent.Loading -> {
                    _loadState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    basePlayer._state.value.clear()//todo don't know if its right method
                    basePlayer._state.value.add(it.data!!.toAudioItem())
                    basePlayer._state.value.play()

                    //add to queue all in episode.next_episodes
                    if (it.data?.next_episodes?.isNotEmpty() == true) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val nextEpisodes = it.data!!.next_episodes.map { nit ->
                                nit.toAudioItem(
                                    it.data!!.podcast.author.name ?: ""
                                )
                            }
                            withContext(Dispatchers.Main) {
                                nextEpisodes.forEach { nnit -> basePlayer._state.value.add(nnit) }

                            }
                            basePlayer.loadNextEpisodes()
                        }
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

    fun pausePlayback() {
        basePlayer._state.value.pause()
    }

    fun likeEpisode() = viewModelScope.launch {
        val episodeId = basePlayer._state.value.currentItem?.albumTitle?.toInt() ?: 0
        CoroutineScope(Dispatchers.IO).launch {
            var response = commonEpisodeRepository.likeEpisode(episodeId)
        }
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