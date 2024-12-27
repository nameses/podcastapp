package com.podcastapp.commonui.errorscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.core.common.theme.ColorPurple500

@Composable
fun ErrorScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = "Something went wrong...",
            style = MaterialTheme.typography.titleLarge,
            color = ColorPurple500,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}