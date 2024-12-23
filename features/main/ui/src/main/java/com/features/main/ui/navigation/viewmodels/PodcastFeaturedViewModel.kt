package com.features.main.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.core.common.ui.HorizontalListItem
import com.features.main.domain.model.Podcast
import com.features.main.domain.model.PodcastList
import com.features.main.domain.use_cases.GetFeaturedPodcastListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastFeaturedViewModel @Inject constructor(
    private val podcastUseCase: GetFeaturedPodcastListUseCase,
    private val
) : ViewModel() {

    private val _state = MutableStateFlow(UiStateHolder<HorizontalListItem>())
    val state: StateFlow<UiStateHolder<HorizontalListItem>> get() = _state

    private var currentPage = 1
    private var lastPage = 1

    init {
        loadPodcasts()
    }

    fun loadPodcasts() = viewModelScope.launch {

        podcastUseCase(currentPage).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    if(it.data == null)
                        return@collect;
                    currentPage = it.data!!.pagination.currentPage
                    lastPage = it.data!!.pagination.lastPage

                    _state.value = UiStateHolder(isSuccess = true, data = it.data.items.map { podcast ->
                        HorizontalListItem(
                            id = podcast.id,
                            title = podcast.title,
                            author = podcast.author,
                            imageUrl = podcast.imageUrl
                        ))
                }

                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }

    }
}