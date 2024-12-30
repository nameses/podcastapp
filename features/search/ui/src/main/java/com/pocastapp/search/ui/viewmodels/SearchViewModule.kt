package com.pocastapp.search.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.EpisodeList
import com.podcastapp.search.domain.models.Language
import com.podcastapp.search.domain.models.SearchParams
import com.podcastapp.search.domain.models.SortOption
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
) : ViewModel() {
    private val _searchState = MutableStateFlow(UiStateHolder<EpisodeList>())
    val searchState: StateFlow<UiStateHolder<EpisodeList>> get() = _searchState

    private val _filtersState = MutableStateFlow(UiStateHolder<AvailableFilters>())
    val filtersState: StateFlow<UiStateHolder<AvailableFilters>> get() = _filtersState

    private val _selectedFiltersState = MutableStateFlow(UiStateHolder<SearchParams>())
    val selectedFiltersState: StateFlow<UiStateHolder<SearchParams>> get() = _selectedFiltersState

    fun setQuery(query: String) {
        if (_selectedFiltersState.value.data != null) _selectedFiltersState.value.data!!.search =
            query
    }

    fun setSortOption(sort: SortOption?) {
        if (_selectedFiltersState.value.data != null && sort != null) _selectedFiltersState.value.data!!.sort =
            sort
    }

    fun setNewFilters(filters: SearchParams) = viewModelScope.launch {
        Log.d("filters", filters.toString())
        Log.d("filters", _selectedFiltersState.value.data.toString())
        if (_selectedFiltersState.value.data == null) {
            _selectedFiltersState.value.data = SearchParams(
                search = null,
                category = null,
                topics = null,
                guests = null,
                language = null,
                sort = null
            )
        }
        _selectedFiltersState.value.data!!.category = filters.category
        _selectedFiltersState.value.data!!.guests = filters.guests
        _selectedFiltersState.value.data!!.topics = filters.topics
        _selectedFiltersState.value.data!!.language = filters.language
    }

    fun refreshSearchResult(query: String) = viewModelScope.launch {
        val searchParams = _selectedFiltersState.value.data ?: SearchParams(
            page = 1,
            search = null,
            category = null,
            topics = null,
            guests = null,
            language = null,
            sort = null,
        )
        searchParams.search = query

        searchPodcastsUseCase(searchParams).collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Loading -> {
                    _searchState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _searchState.value = UiStateHolder(isSuccess = true, data = uiEvent.data)
                }

                is UiEvent.Error -> {
                    _searchState.value =
                        UiStateHolder(message = uiEvent.message ?: "Check internet connection")
                }
            }
        }
    }

    fun getAvailableFilters() = viewModelScope.launch {
        getAvailableFiltersUseCase().collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Loading -> {
                    _filtersState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _filtersState.value = UiStateHolder(isSuccess = true, data = uiEvent.data)
                }

                is UiEvent.Error -> {
                    _filtersState.value =
                        UiStateHolder(message = uiEvent.message ?: "Unknown error")
                }
            }
        }
    }
}