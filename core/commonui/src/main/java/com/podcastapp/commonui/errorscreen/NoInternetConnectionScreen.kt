package com.podcastapp.commonui.errorscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.core.common.theme.ColorLittleBitGray

@Composable
fun NoInternetConnectionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color(0x30CFCFCF), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Offline mode",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "No Internet Connection",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Open profile to listen saved episodes",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}