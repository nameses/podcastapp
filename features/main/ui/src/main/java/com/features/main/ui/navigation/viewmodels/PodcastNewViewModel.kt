package com.features.main.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.repo.PodcastType
import com.features.main.domain.use_cases.GetFeaturedPodcastListUseCase
import com.features.main.domain.use_cases.GetNewPodcastListUseCase
import com.podcastapp.commonui.model.HorizontalListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastNewViewModel @Inject constructor(
//    private val podcastUseCase: GetNewPodcastListUseCase
    private val podcastRepository: PodcastRepository
) : ViewModel() {

    private val _podcasts = MutableStateFlow(UiStateHolder<List<HorizontalListItem>>())
    val podcasts: StateFlow<UiStateHolder<List<HorizontalListItem>>> get() = _podcasts

    private val _state = MutableStateFlow(UiStateHolder<Unit>())
    val state: StateFlow<UiStateHolder<Unit>> get() = _state

    private val currentPage = MutableStateFlow(1)
    private val lastPage = MutableStateFlow(1)
//    private var currentPage = 1
//    private var lastPage = 1

    fun loadPodcasts() = viewModelScope.launch {
        if(currentPage.value > lastPage.value) return@launch;

        _podcasts.value = UiStateHolder(isLoading = true)

        var response = podcastRepository.getPodcastList(PodcastType.New, currentPage.value)

        when(response){
            is RepoEvent.Success -> {
                if (response.data == null) return@launch;

                currentPage.value = response.data!!.pagination.currentPage + 1
                lastPage.value = response.data!!.pagination.lastPage

                val podcasts = response.data!!.items.map { podcast ->
                    HorizontalListItem(
                        id = podcast.id,
                        title = podcast.title,
                        author = podcast.author,
                        imageUrl = podcast.imageUrl,
                        isInitiallySaved = podcast.isSaved
                    )
                }

                _podcasts.value = UiStateHolder(isSuccess = true, data = podcasts)
            }
            is RepoEvent.Error -> {
                _podcasts.value =
                    UiStateHolder(message = response.message.toString(), errors = response.errors)
            }
        }

//        podcastUseCase(currentPage).collect { it ->
//
//            when (it) {
//                is UiEvent.Loading -> {
//                    _state.value = UiStateHolder(isLoading = true)
//                }
//                is UiEvent.Success -> {
//                    if (it.data == null) return@collect;
//
//                    currentPage = it.data!!.pagination.currentPage + 1
//                    lastPage = it.data!!.pagination.lastPage
//
//                    _podcasts.value += it.data!!.items.map { podcast ->
//                        HorizontalListItem(
//                            id = podcast.id,
//                            title = podcast.title,
//                            author = podcast.author,
//                            imageUrl = podcast.imageUrl,
//                            isInitiallySaved = podcast.isSaved
//                        )
//                    }
//
//                    _state.value = UiStateHolder(isSuccess = true, data = Unit)
//                }
//
//                is UiEvent.Error -> {
//                    _state.value =
//                        UiStateHolder(message = it.message.toString(), errors = it.errors)
//                }
//            }
//        }
    }
}