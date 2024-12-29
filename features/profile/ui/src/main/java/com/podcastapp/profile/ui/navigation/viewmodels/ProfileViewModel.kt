package com.podcastapp.profile.ui.navigation.viewmodels

import android.media.session.MediaSession.Token
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.core.common.services.TokenManager
import com.podcastapp.commonrepos.repos.DownloadRepository
import com.podcastapp.commonui.model.HorizontalListItem
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository,
    private val profileUseCase: ProfileUseCase,
    private val tokenManager:TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<UserFull>())
    val state: StateFlow<UiStateHolder<UserFull>> get() = _state

    val ifContainsLikedEpisodes = MutableStateFlow(false)
    val ifContainsSavedPodcasts = MutableStateFlow(false)
    val ifContainsDownloadedEpisodes = MutableStateFlow(false)

    init{
        reloadState()
    }

    fun renewListStates() = viewModelScope.launch {
        if(_state.value.isSuccess){
            ifContainsLikedEpisodes.value = _state.value.data?.likedEpisodes?.isNotEmpty() ?: false
            ifContainsSavedPodcasts.value = _state.value.data?.savedPodcasts?.isNotEmpty() ?: false
        }
    }

    fun clearToken() = viewModelScope.launch {
        tokenManager.clearToken()
    }

    fun reloadState() = viewModelScope.launch {
        _state.value = UiStateHolder(isLoading = true)
        getProfile()
    }

    fun getProfile() = viewModelScope.launch {
        profileUseCase().collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _state.value = UiStateHolder(isSuccess = true, data = it.data)
                    renewListStates()
                }

                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }

        val downloadedEpisodes = downloadRepository.getEpisodes()
        if(downloadedEpisodes.isNotEmpty()) ifContainsDownloadedEpisodes.value = true
    }

    private val _savedPodcasts = MutableStateFlow<List<HorizontalListItem>>(emptyList())
    val savedPodcasts: StateFlow<List<HorizontalListItem>> get() = _savedPodcasts

    fun loadSavedPodcasts() = viewModelScope.launch {
        if (_state.value.isSuccess) {
            _savedPodcasts.value = _state.value.data!!.savedPodcasts.map { podcast ->
                HorizontalListItem(
                    id = podcast.id,
                    title = podcast.title,
                    author = podcast.author,
                    imageUrl = podcast.imageUrl,
                    isInitiallySaved = podcast.isSaved
                )
            }
        }
    }

    private val _likedEpisodes = MutableStateFlow<List<HorizontalListItem>>(emptyList())
    val likedEpisodes: StateFlow<List<HorizontalListItem>> get() = _likedEpisodes

    fun loadLikedEpisodes() = viewModelScope.launch {
        if (_state.value.isSuccess) {
            _likedEpisodes.value = _state.value.data!!.likedEpisodes.map { podcast ->
                HorizontalListItem(
                    id = podcast.id,
                    title = podcast.title,
                    author = podcast.author,
                    imageUrl = podcast.imageUrl,
                    isInitiallySaved = false
                )
            }
        }
    }

    private val _downloadedEpisodes = MutableStateFlow<List<HorizontalListItem>>(emptyList())
    val downloadedEpisodes: StateFlow<List<HorizontalListItem>> get() = _downloadedEpisodes

    fun loadDownloadedEpisodes() = viewModelScope.launch {
        val downloadedEpisodes = downloadRepository.getEpisodes()
        Timber.tag("DOWNLOADED_EPISODES").d(downloadedEpisodes.toString())
        _downloadedEpisodes.value = downloadedEpisodes.map { podcast ->
            HorizontalListItem(
                id = podcast.id,
                title = podcast.title,
                author = podcast.author,
                imageUrl = podcast.absolutePathImage,
                isInitiallySaved = false
            )
        }
    }
}