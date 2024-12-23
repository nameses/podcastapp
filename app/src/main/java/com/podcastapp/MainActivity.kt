package com.podcastapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.common.constants.AuthFeature
import com.core.common.services.TokenManager
import com.podcastapp.navigation.AppNavGraph
import com.podcastapp.navigation.NavigationProvider
import com.podcastapp.ui.common.BottomNavigationBar
import com.podcastapp.ui.theme.PodcastappTheme
import dagger.hilt.android.AndroidEntryPoint
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun App(
    navHostController: NavHostController,
    navigationProvider: NavigationProvider,
    tokenManager: TokenManager
) {
    //bottom bar
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentRoute) {
        //not showing bar while logging in
        AuthFeature.loginScreen, AuthFeature.registerScreen -> false
        else -> true
    }

    //register app navigation graph
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if(showBottomBar && currentRoute != null){
                BottomNavigationBar(navHostController, currentRoute)
            }
        }) {
        AppNavGraph(
            navController = navHostController,
            navigationProvider = navigationProvider,
            tokenManager = tokenManager
        )
    }
}