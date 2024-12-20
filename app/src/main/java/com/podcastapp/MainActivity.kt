package com.podcastapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.core.common.navigation_constants.AuthFeature
import com.core.common.navigation_constants.MainFeature
import com.core.common.services.TokenManager
import com.podcastapp.navigation.AppNavGraph
import com.podcastapp.navigation.NavigationProvider
import com.podcastapp.ui.theme.PodcastappTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationProvider: NavigationProvider
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PodcastappTheme {
                val navController = rememberNavController()
                App(navHostController = navController, navigationProvider, tokenManager)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(navHostController: NavHostController, navigationProvider: NavigationProvider,tokenManager : TokenManager) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        AppNavGraph(navController = navHostController, navigationProvider = navigationProvider, tokenManager = tokenManager)
    }
}