package com.pocastapp.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiStateHolder
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.EpisodeList
import com.podcastapp.search.domain.models.SearchParams
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.podcastapp.search.domain.use_cases.GetAvailableFiltersUseCase
import com.podcastapp.search.domain.use_cases.SearchPodcastsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModule @Inject constructor(
    private val getAvailableFiltersUseCase: GetAvailableFiltersUseCase,
    private val searchPodcastsUseCase: SearchPodcastsUseCase
): ViewModel() {
    private val _searchState = MutableStateFlow(UiStateHolder<EpisodeList>())
    val searchState: StateFlow <UiStateHolder<EpisodeList>> get() = _searchState

    private val _filtersState = MutableStateFlow(UiStateHolder<AvailableFilters>())
    val filtersState: StateFlow <UiStateHolder<AvailableFilters>> get() = _filtersState

    private val _selectedFiltersState = MutableStateFlow(UiStateHolder<SearchParams>())
    val selectedFiltersState: StateFlow <UiStateHolder<SearchParams>> get() = _selectedFiltersState

    fun refreshSearchResult() = viewModelScope.launch {

    }

    fun getAvailableFilters() = viewModelScope.launch {

    }
}