package com.features.main.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.features.main.domain.use_cases.GetFeaturedPodcastListUseCase
import com.podcastapp.commonui.model.HorizontalListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastFeaturedViewModel @Inject constructor(
    private val podcastUseCase: GetFeaturedPodcastListUseCase
) : ViewModel() {

    private val _podcasts = MutableStateFlow<List<HorizontalListItem>>(emptyList())
    val podcasts: StateFlow<List<HorizontalListItem>> get() = _podcasts

    private val _state = MutableStateFlow(UiStateHolder<Unit>())
    val state: StateFlow<UiStateHolder<Unit>> get() = _state

    private var currentPage = 1
    private var lastPage = 1

//    init {
//        loadPodcasts()
//    }

    fun loadPodcasts() = viewModelScope.launch {
        podcastUseCase(currentPage).collect { it ->
            if(currentPage > lastPage) return@collect;
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }
                is UiEvent.Success -> {
                    if (it.data == null) return@collect;

                    currentPage = it.data!!.pagination.currentPage + 1
                    lastPage = it.data!!.pagination.lastPage

                    _state.value = UiStateHolder(isSuccess = true, data = Unit)

                    _podcasts.value += it.data!!.items.map { podcast ->
                        HorizontalListItem(
                            id = podcast.id,
                            title = podcast.title,
                            author = podcast.author,
                            imageUrl = podcast.imageUrl,
                            isInitiallySaved = podcast.isSaved
                        )
                    }
                }

                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }
}