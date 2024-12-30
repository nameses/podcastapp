package com.podcastapp.commonui

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.podcastapp.commonui.model.HorizontalListItem
import com.podcastapp.commonui.viewmodels.HorizontalListItemViewModel


@OptIn(ExperimentalCoilApi::class)
@Composable
fun HorizontalListItem(
    item: HorizontalListItem,
    viewModel: HorizontalListItemViewModel,
    onClick: () -> Unit,
    showAddToSavedFragment: Boolean,
    onSaveStateChanged: (Int, Boolean) -> Unit
) {
    val savedState by viewModel.savedState.collectAsState()
    val isSaved = savedState[item.id] ?: item.isInitiallySaved

    Column(modifier = Modifier
        .width(140.dp)
        .padding(8.dp)
        .clickable { onClick() }) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl ?: R.drawable.default_item_list).crossfade(true).build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )


            if (showAddToSavedFragment) {
                AddToSavedFragment(
                    id = item.id,
                    viewModel = viewModel,
                    isInitiallySaved = isSaved,//item.isInitiallySaved,
                    modifier = Modifier.align(Alignment.BottomStart),
                    onSaveStateChanged = onSaveStateChanged
                )
            }
        }

        Text(
            text = item.title,
            maxLines = 1,
            minLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = item.author,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
