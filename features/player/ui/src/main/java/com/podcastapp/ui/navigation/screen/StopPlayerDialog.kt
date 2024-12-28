package com.podcastapp.ui.navigation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.core.common.theme.ColorPurple500

@Composable
fun TimerDialog(
    onDismiss: () -> Unit, onSetTimer: (Long) -> Unit
) {
    var minutesInput by remember { mutableStateOf("") }
    var secondsInput by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            "Set Timer",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }, text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enter minutes (0-60) and seconds (0-60):")

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = minutesInput,
                        onValueChange = {
                            minutesInput = it.filter { char -> char.isDigit() }.take(2)
                                .takeIf { input -> input.toIntOrNull() ?: 0 <= 60 } ?: ""
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxSize(),
                        textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    )
                }

                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = secondsInput,
                        onValueChange = {
                            secondsInput = it.filter { char -> char.isDigit() }.take(2)
                                .takeIf { input -> input.toIntOrNull() ?: 0 < 60 } ?: ""
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxSize(),
                        textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    )
                }
            }
        }
    }, confirmButton = {
        Button(onClick = {
            val minutes = minutesInput.toIntOrNull() ?: 0
            val seconds = secondsInput.toIntOrNull() ?: 0
            if (minutes in 0..60 && seconds in 0..59) {
                val totalDuration = (minutes * 60 + seconds) * 1000L
                onSetTimer(totalDuration)
                onDismiss()
            }
        }, colors = ButtonDefaults.buttonColors(ColorPurple500)) {
            Text("Set")
        }
    }, dismissButton = {
        Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(ColorPurple500)) {
            Text("Cancel")
        }
    })
}

