package com.podcastapp.ui.navigation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.core.common.theme.ColorLittleBitGray
import com.core.common.theme.ColorWhite

@Composable
fun PlayerScreenSilhouette() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
        )

        // Artwork and track info placeholder
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Artwork placeholder
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Track info placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
            )
        }

        // Player controls placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Collapsible section placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(ColorLittleBitGray, RoundedCornerShape(8.dp))
        )
    }
}
