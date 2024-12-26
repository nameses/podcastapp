package com.podcastapp.profile.ui.navigation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.core.common.theme.ColorPurple500
import com.podcastapp.profile.ui.R
import com.podcastapp.profile.ui.navigation.viewmodels.EditProfileViewModel
import java.io.File

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val username = remember { mutableStateOf(state.data?.username ?: "") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageFile = remember { mutableStateOf<File?>(null) }
    val localContext = LocalContext.current
    val showPremiumDialog = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
        uri?.let {
            viewModel.onImageSelected(uri)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorPurple500)
                    .height(200.dp)
            ) {
                imageUri.value?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .align(Alignment.Center)
                    )
                } ?: Image(
                    painter = rememberImagePainter(
                        data = state.data?.imageUrl ?: R.drawable.default_avatar
                    ),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .align(Alignment.Center)
                )

                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(ColorPurple500, CircleShape)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Filled.AddAPhoto, contentDescription = "Pick Image", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Button(
                onClick = { showPremiumDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Upgrade to Premium")
            }


            Button(
                onClick = {
                    imageFile.value?.let {
                        viewModel.editProfile(username.value, it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Save")
            }

            if (state.errors?.isNotEmpty() == true) {
                Text(
                    text = state.errors.toString(),
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (showPremiumDialog.value) {
            PremiumDialog(
                onDismiss = { showPremiumDialog.value = false },
                onPurchase = { cvv, cardNumber, expirationDate, cardHolder ->
                    viewModel.onPurchasePremium(cvv, cardNumber, expirationDate, cardHolder)
                }
            )
        }
    }
}