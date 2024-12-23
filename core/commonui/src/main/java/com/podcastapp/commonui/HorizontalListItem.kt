package com.podcastapp.commonui

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.podcastapp.commonui.model.HorizontalListItem


@OptIn(ExperimentalCoilApi::class)
@Composable
fun HorizontalListItem(
    item: HorizontalListItem,
    onClick: () -> Unit,
    showAddToSavedFragment: Boolean
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = item.imageUrl ?: R.drawable.default_item_list,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            if (showAddToSavedFragment) {
                AddToSavedFragment(
                    id = item.id,
                    isInitiallySaved = item.isInitiallySaved,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }

        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { onClick() }
        )

        Text(
            text = item.author,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}