package com.features.main.ui.navigation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.features.main.domain.model.PodcastDTO
import com.features.main.domain.use_cases.GetPopularPodcastListUseCase
import com.features.main.ui.navigation.viewmodels.PodcastFeaturedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getPodcastListUseCase: GetPopularPodcastListUseCase) :
    ViewModel() {
    //todo
}



@Composable
fun PodcastListScreen(viewModel: PodcastFeaturedViewModel) {
    val podcasts by viewModel.podcasts.collectAsState()
    val state by viewModel.loadState.collectAsState()
    val listState = rememberLazyListState()

    // Trigger to load more when scrolled to the end
    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        if (!state.isLoading  && listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == podcasts.size - 1) {
            viewModel.loadPodcasts()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(64.dp))

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(podcasts) { podcast ->
                PodcastCard(podcast = podcast)
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun PodcastCard(podcast: PodcastDTO) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(
                text = podcast.title,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = podcast.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}