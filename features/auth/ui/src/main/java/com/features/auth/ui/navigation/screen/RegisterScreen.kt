package com.features.auth.ui.navigation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.core.common.services.isNetworkAvailable
import com.features.auth.ui.navigation.viewmodels.RegisterViewModel
import com.podcastapp.commonui.errorscreen.NoInternetConnectionScreen

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onLoginClick: () -> Unit,
    onSuccess: () -> Unit,
) {
    if (isNetworkAvailable(LocalContext.current)) {
        val authState by viewModel.authState.collectAsState()
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }

        var redirected by remember { mutableStateOf(false) }

        if (authState.isSuccess && !redirected) {
            onSuccess()
            redirected = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Section with "Sign Up" and "Log In" link
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                Text(
                    text = "Log In",
                    color = Color(0xFF7C4DFF),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onLoginClick() },
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            authState.errors?.get("username")?.forEach { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            authState.errors?.get("email")?.forEach { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                trailingIcon = {
                    Text(
                        text = if (isPasswordVisible) "Hide" else "Show",
                        color = Color(0xFF7C4DFF),
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible }
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            authState.errors?.get("password")?.forEach { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                trailingIcon = {
                    Text(
                        text = if (isPasswordVisible) "Hide" else "Show",
                        color = Color(0xFF7C4DFF),
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible }
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                Text(
                    text = "Passwords are not matching",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            Button(
                onClick = { viewModel.register(username, email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF))
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Loading Indicator
            if (authState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            // Error Message
            if (authState.message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = authState.message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    } else {
        NoInternetConnectionScreen()
    }
}


