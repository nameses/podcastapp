package com.podcastapp.profile.ui.navigation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.session.MediaSession.Token
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.core.common.services.TokenManager
import com.core.common.services.isNetworkAvailable
import com.podcastapp.commonrepos.repos.DownloadRepository
import com.podcastapp.commonui.errorscreen.NoInternetConnectionScreen
import com.podcastapp.commonui.model.HorizontalListItem
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadRepository: DownloadRepository,
    private val profileUseCase: ProfileUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<UserFull>())
    val state: StateFlow<UiStateHolder<UserFull>> get() = _state

    val ifContainsLikedEpisodes = MutableStateFlow(false)
    val ifContainsSavedPodcasts = MutableStateFlow(false)
    val ifContainsDownloadedEpisodes = MutableStateFlow(false)

    init {
        reloadState()
    }

    fun renewListStates() = viewModelScope.launch {
        if (_state.value.isSuccess) {
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
        if (!isNetworkAvailable(context)) {
            _state.value = UiStateHolder(isLoading = true)

            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)
            val imageUrl = sharedPreferences.getString("imageUrl", null)
            val premium = sharedPreferences.getBoolean("premium", false)

            _state.value = UiStateHolder(
                isSuccess = true, data = UserFull(
                    "", username ?: "", imageUrl, premium, emptyList(), emptyList()
                )
            )
        } else {
            profileUseCase().collect { it ->
                when (it) {
                    is UiEvent.Loading -> {
                        _state.value = UiStateHolder(isLoading = true)
                    }

                    is UiEvent.Success -> {
                        _state.value = UiStateHolder(isSuccess = true, data = it.data)
                        renewListStates()

                        val sharedPreferences =
                            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", it.data?.username)
                        editor.putString("imageUrl", it.data?.imageUrl)
                        editor.putBoolean("premium", it.data?.premium ?: false)
                        editor.apply()

                        // Download user image
                        it.data?.imageUrl?.let { imageUrl ->
                            downloadImage(imageUrl, it.data?.username ?: "test")
                        }
                    }

                    is UiEvent.Error -> {
                        _state.value =
                            UiStateHolder(message = it.message.toString(), errors = it.errors)
                    }
                }
            }
        }

        val downloadedEpisodes = downloadRepository.getEpisodes()
        if (downloadedEpisodes.isNotEmpty()) ifContainsDownloadedEpisodes.value = true
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

    fun downloadImage(imageUrl: String, username: String) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context).data(imageUrl).target { drawable ->
            val bitmap = (drawable as BitmapDrawable).bitmap
            saveImageToStorage(bitmap, username)
        }.build()

        imageLoader.enqueue(request)
    }

    fun saveImageToStorage(bitmap: Bitmap, username: String) {
        try {
            val file = File(context.filesDir, "$username.jpg")
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}