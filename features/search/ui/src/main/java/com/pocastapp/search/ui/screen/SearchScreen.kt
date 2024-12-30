package com.pocastapp.search.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.core.common.constants.EpisodeDetailedFeature
import com.core.common.constants.PlayerFeature
import com.core.common.services.isNetworkAvailable
import com.core.common.theme.ColorPurple500
import com.pocastapp.search.ui.viewmodels.SearchViewModule
import com.podcastapp.commonui.errorscreen.ErrorScreen
import com.podcastapp.commonui.errorscreen.NoInternetConnectionScreen
import com.podcastapp.search.domain.models.AvailableFilters
import com.podcastapp.search.domain.models.Language
import com.podcastapp.search.domain.models.SearchParams
import com.podcastapp.search.domain.models.SearchedEpisode
import com.podcastapp.search.domain.models.SortOption
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    viewModel: SearchViewModule,
    navController: NavHostController
) {
    if (!isNetworkAvailable(LocalContext.current)) {
        NoInternetConnectionScreen()
    } else {
        val searchState by viewModel.searchState.collectAsState()
        val filtersState by viewModel.filtersState.collectAsState()
        val selectedFiltersState by viewModel.selectedFiltersState.collectAsState()

        var searchQuery by remember { mutableStateOf("") }
        var isFilterPopupVisible by remember { mutableStateOf(false) }
        var selectedSortOption by remember { mutableStateOf<SortOption?>(null) }

        LaunchedEffect(Unit) {
            viewModel.getAvailableFilters()
        }

        LaunchedEffect(searchQuery) {
            delay(2000L)
            viewModel.refreshSearchResult(searchQuery)
        }

        if (filtersState.isLoading) {
            SilhouetteScreen()
        } else if (filtersState.isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Row with Sort on left and Filter on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sort Button on Left
                    var isSortDropdownExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth(0.7f)) {
                        OutlinedButton(onClick = {
                            isSortDropdownExpanded = !isSortDropdownExpanded
                        }) {
                            Text(selectedSortOption?.displayName ?: "Sort By")
                        }
                        DropdownMenu(
                            expanded = isSortDropdownExpanded,
                            onDismissRequest = { isSortDropdownExpanded = false }
                        ) {
                            SortOption.entries.forEach { sortOption ->
                                DropdownMenuItem(
                                    text = { Text(sortOption.displayName) },
                                    onClick = {
                                        selectedSortOption = sortOption
                                        viewModel.setSortOption(sortOption)
                                        isSortDropdownExpanded = false
                                        viewModel.refreshSearchResult(searchQuery)
                                    })
                            }
                        }
                    }

                    // Filter Button on Right
                    Button(onClick = { isFilterPopupVisible = true }) {
                        Text("Filter")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.refreshSearchResult(searchQuery)
                        },
                        placeholder = { Text("Search by keyword") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Episodes List
                if (searchState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )
                } else if (searchState.isSuccess) {
                    LazyColumn {
                        if (searchState.data != null) items(searchState.data!!.items) { episode ->
                            EpisodeItem(episode = episode, onPlayClick = {
                                navController.navigate("${PlayerFeature.playerScreen}/${episode.id}")
                            }, onEpisodeClick = {
                                navController.navigate("${EpisodeDetailedFeature.episodeScreen}/${episode.id}")
                            }) //todo
                        }
                    }
                }
            }

            if (isFilterPopupVisible) {
                FilterPopup(filters = filtersState.data,
                    selectedFilters = selectedFiltersState.data,
                    onApply = { filters ->
                        viewModel.setNewFilters(filters)
                        isFilterPopupVisible = false
                        viewModel.refreshSearchResult(searchQuery)
                    },
                    onDismiss = { isFilterPopupVisible = false })
            }
        } else {
            ErrorScreen()
        }
    }
}


@Composable
fun FilterPopup(
    filters: AvailableFilters?,
    selectedFilters: SearchParams?,
    onApply: (SearchParams) -> Unit,
    onDismiss: () -> Unit
) {
    if (filters == null) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filters", style = MaterialTheme.typography.titleLarge)

                // Category
                Text("Category")
                var selectedCategory by remember { mutableStateOf<Int?>(selectedFilters?.category) }
                var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { isCategoryDropdownExpanded = true }) {
                        Text(
                            filters.categories.find { it.id == selectedCategory }?.name
                                ?: "Select Category"
                        )
                    }
                    DropdownMenu(expanded = isCategoryDropdownExpanded,
                        onDismissRequest = { isCategoryDropdownExpanded = false }) {
                        filters.categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                selectedCategory = category.id
                                isCategoryDropdownExpanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Topics")
                var selectedTopics =
                    remember { mutableStateListOf<Int>().apply { addAll(selectedFilters?.topics.orEmpty()) } }
                var isTopicsDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { isTopicsDropdownExpanded = true }) {
                        Text("Select Topics (${selectedTopics.size})")
                    }
                    DropdownMenu(expanded = isTopicsDropdownExpanded,
                        onDismissRequest = { isTopicsDropdownExpanded = false }) {
                        filters.topics.forEach { topic ->
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = selectedTopics.contains(topic.id),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) selectedTopics.add(topic.id)
                                            else selectedTopics.remove(topic.id)
                                        })
                                    Text(topic.name)
                                }
                            }, onClick = {})
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Guests")
                var selectedGuests =
                    remember { mutableStateListOf<Int>().apply { addAll(selectedFilters?.guests.orEmpty()) } }
                var isGuestsDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { isGuestsDropdownExpanded = true }) {
                        Text("Select Guests (${selectedGuests.size})")
                    }
                    DropdownMenu(expanded = isGuestsDropdownExpanded,
                        onDismissRequest = { isGuestsDropdownExpanded = false }) {
                        filters.guests.forEach { guest ->
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = selectedGuests.contains(guest.id),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) selectedGuests.add(guest.id)
                                            else selectedGuests.remove(guest.id)
                                        })
                                    Text(guest.name)
                                }
                            }, onClick = {})
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Language
                Text("Language")
                var selectedLanguage by remember { mutableStateOf<Language?>(selectedFilters?.language) }
                var isLanguageDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { isLanguageDropdownExpanded = true }) {
                        Text(selectedLanguage?.name ?: "Select Language")
                    }
                    DropdownMenu(expanded = isLanguageDropdownExpanded,
                        onDismissRequest = { isLanguageDropdownExpanded = false }) {
                        Language.entries.forEach { language ->
                            DropdownMenuItem(text = { Text(language.name) }, onClick = {
                                selectedLanguage = language
                                isLanguageDropdownExpanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons Row (Apply and Reset)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        val newFilters = SearchParams(
                            search = null,
                            category = selectedCategory,
                            topics = selectedTopics.toList(),
                            guests = selectedGuests.toList(),
                            language = selectedLanguage,
                            sort = null
                        )
                        onApply(newFilters)
                    }) {
                        Text("Apply")
                    }

                    // Reset button on the right
                    OutlinedButton(onClick = {
                        selectedCategory = null
                        selectedTopics.clear()
                        selectedGuests.clear()
                        selectedLanguage = null

                    }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: SearchedEpisode,
    onPlayClick: (Int) -> Unit,
    onEpisodeClick: (Int) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onEpisodeClick(episode.id) }
        .padding(8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Left block: Image
        AsyncImage(
            model = episode.image,
            contentDescription = episode.title,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${episode.duration}m",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = episode.description,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Box(modifier = Modifier.clickable(enabled = false) {}) {
                    IconButton(
                        onClick = { onPlayClick(episode.id) }, modifier = Modifier
                            .background(
                                color = ColorPurple500, shape = CircleShape
                            )
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
