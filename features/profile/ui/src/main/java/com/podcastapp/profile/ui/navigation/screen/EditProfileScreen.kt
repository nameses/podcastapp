package com.podcastapp.profile.ui.navigation.screen

import android.net.Uri
import android.provider.ContactsContract.Profile
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.core.common.constants.ProfileFeature
import com.core.common.model.UiStateHolder
import com.core.common.services.getNavResultCallback
import com.core.common.services.popBackStackWithResult
import com.core.common.services.setNavResultCallback
import com.core.common.theme.ColorPurple500
import com.core.common.theme.ColorWhite
import com.podcastapp.commonui.errorscreen.ErrorScreen
import com.podcastapp.profile.ui.R
import com.podcastapp.profile.ui.navigation.viewmodels.EditProfileViewModel
import java.io.File

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel, onBack: () -> Unit, navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val editState by viewModel.editState.collectAsState()
    val premiumPurchaseState by viewModel.premiumPurchaseState.collectAsState()

    val usernameState by viewModel.usernameState.collectAsState()

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showPremiumDialog = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
        uri?.let {
            viewModel.onImageSelected(uri)
        }
    }

    if (editState.isSuccess) {
        Snackbar(content = { Text("Profile changes saved!") })
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        state.isSuccess -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorPurple500)
                            .height(340.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(ColorPurple500)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(ColorWhite)
                                .align(Alignment.BottomCenter)
                        )

                        IconButton(
                            onClick = onBack, modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = ColorWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .align(Alignment.Center)
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = imageUri.value ?: state.data?.imageUrl
                                    ?: R.drawable.default_avatar
                                ),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape)
                                    .border(4.dp, ColorWhite, CircleShape)
                            )

                            IconButton(
                                onClick = { launcher.launch("image/*") },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(ColorPurple500, CircleShape)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.AddAPhoto,
                                    contentDescription = "Pick Image",
                                    tint = Color.White
                                )
                            }
                        }

                        OutlinedTextField(
                            value = usernameState,
                            onValueChange = { viewModel.setUsernameState(it) },
                            label = { Text("Username") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }
                    if (state.data?.premium == true) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEEEEEE))
                        ) {
                            Text(
                                text = "Premium activated",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 24.dp, bottom = 24.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEEEEEE))
                        ) {
                            Text(
                                text = "Upgrade to premium",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 24.dp, bottom = 24.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Listen to podcasts on the go, even on a locked phone. Download podcasts and listen on long journeys.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(horizontal = 16.dp)
                            )

                            Button(
                                onClick = { showPremiumDialog.value = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp, end = 16.dp, bottom = 24.dp, top = 16.dp
                                    ),
                                colors = ButtonDefaults.buttonColors(ColorPurple500)
                            ) {
                                Text(
                                    "Buy premium", style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.editProfile(usernameState, viewModel.imageFile.value)

                            //navController.previousBackStackEntry?.savedStateHandle?.set("reload", true)
                            navController.navigate(ProfileFeature.profileScreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(ColorPurple500)
                    ) {
                        Text("Save", style = MaterialTheme.typography.titleMedium)
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
                    PremiumDialog(onDismiss = {
                        showPremiumDialog.value = false;
                        navController.navigate(ProfileFeature.profileScreen) {
                            popUpTo(ProfileFeature.profileScreen) { inclusive = true }
                        }
                    }, onPurchase = { cvv, cardNumber, expirationDate, cardHolder ->
                        viewModel.onPurchasePremium(cvv, cardNumber, expirationDate, cardHolder)
                    })
                }
            }
        }

        else -> {
            ErrorScreen()
        }
    }


}