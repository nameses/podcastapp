package com.podcastapp.search.domain.use_cases

import android.util.Log
import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.repo.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAvailableFiltersUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    operator fun invoke() = flow<UiEvent<AvailableFilters>> {
        emit(UiEvent.Loading())
        val response = searchRepository.getAvailableFilters()
        Log.d("RESPONSE_FILTERS", response.data.toString())
        when (response) {
            is RepoEvent.Success -> if (response.data != null) emit(UiEvent.Success(response.data!!))
            is RepoEvent.Error -> emit(UiEvent.Error(response.message!!, response.errors))
        }

    }.catch {
        emit(UiEvent.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}