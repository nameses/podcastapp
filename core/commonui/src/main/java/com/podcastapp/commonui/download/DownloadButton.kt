package com.podcastapp.commonui.download

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.LaunchedEffect
import com.core.common.theme.ColorLittleBitGray
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.podcastapp.commonui.model.DownloadState
import com.podcastapp.commonrepos.model.EpisodeDownload
import com.podcastapp.commonui.viewmodels.DownloadViewModel

@Composable
fun DownloadButton(episode: EpisodeDownload) {
    val viewModel: DownloadViewModel = hiltViewModel()

    val downloadState by viewModel.downloadState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.changeToDownloadedStateIfDownloaded(episode.id)
    }

    Box(
        modifier = Modifier.size(40.dp)
    ) {
        IconButton(
            onClick = {
                when (downloadState) {
                    DownloadState.NOT_DOWNLOADED -> viewModel.download(episode)
                    DownloadState.DOWNLOADED -> viewModel.cancelDownload(episode.id)
                    else -> Unit
                }
            },
            enabled = downloadState != DownloadState.DOWNLOADING && downloadState != DownloadState.CANCELING,
            modifier = Modifier.fillMaxWidth(),
            colors = IconButtonColors(
                containerColor = ColorWhite,
                contentColor = ColorPurple500,
                disabledContainerColor = ColorWhite,
                disabledContentColor = ColorLittleBitGray
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                when (downloadState) {
                    DownloadState.DOWNLOADING -> {
                        Icon(
                            Icons.Default.Downloading,
                            contentDescription = "Cancel Download"
                        )
                    }

                    DownloadState.DOWNLOADED -> {
                        Icon(
                            Icons.Default.DownloadDone,
                            contentDescription = "Cancel Download"
                        )
                    }

                    DownloadState.NOT_DOWNLOADED -> {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "Download"
                        )
                    }

                    DownloadState.CANCELING -> {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = "Download"
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
