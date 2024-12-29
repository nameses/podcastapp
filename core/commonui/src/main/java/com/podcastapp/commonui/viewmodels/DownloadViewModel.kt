package com.podcastapp.commonui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podcastapp.commonui.model.DownloadState
import com.podcastapp.commonrepos.model.EpisodeDownload
import com.podcastapp.commonrepos.repos.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(private val downloadRepository: DownloadRepository) :
    ViewModel() {

    private val _downloadState = MutableStateFlow(DownloadState.NOT_DOWNLOADED)
    val downloadState: StateFlow<DownloadState> get() = _downloadState

    fun changeToDownloadedStateIfDownloaded(episodeId: Int): Unit {
        var ifExist: Boolean = false
        viewModelScope.launch {
            ifExist = downloadRepository.checkIfExist(episodeId)
            Timber.tag("DB").d(ifExist.toString())

            if (ifExist) {
                _downloadState.value = DownloadState.DOWNLOADED
            }
        }
    }

    fun download(episode: EpisodeDownload) {
        _downloadState.value = DownloadState.DOWNLOADING

        viewModelScope.launch {
            downloadRepository.downloadEpisode(episode)

            _downloadState.value = DownloadState.DOWNLOADED
        }
    }

    fun cancelDownload(episodeId: Int) {
        viewModelScope.launch {
            downloadRepository.cancelDownload(episodeId)
        }

        _downloadState.value = DownloadState.NOT_DOWNLOADED
    }
}