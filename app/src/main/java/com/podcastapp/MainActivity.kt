package com.podcastapp

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.common.constants.AuthFeature
import com.core.common.constants.PlayerFeature
import com.core.common.constants.ProfileFeature
import com.core.common.services.TokenManager
import com.core.common.theme.ColorPurple500
import com.podcastapp.navigation.AppNavGraph
import com.podcastapp.navigation.NavigationProvider
import com.podcastapp.ui.common.BottomNavigationBar
import com.podcastapp.ui.navigation.viewmodels.BasePlayerViewModel
import com.podcastapp.ui.navigation.viewmodels.PlayerViewModel
import com.podcastapp.ui.theme.PodcastappTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationProvider: NavigationProvider

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var basePlayerViewModel: BasePlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PodcastappTheme {
                val navController = rememberNavController()
                App(
                    navHostController = navController,
                    navigationProvider,
                    tokenManager,
                    basePlayerViewModel
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun App(
    navHostController: NavHostController,
    navigationProvider: NavigationProvider,
    tokenManager: TokenManager,
    basePlayerViewModel: BasePlayerViewModel
) {
    //bottom bar
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("ROUTES", currentRoute ?: "null")
    val showBottomBar = when (currentRoute) {
        AuthFeature.loginScreen, AuthFeature.registerScreen, PlayerFeature.playerScreen, PlayerFeature.playerScreenDeepLink, PlayerFeature.playerWithIdScreen -> false
        else -> true
    }

    //adjust status bar color
    val statusBarColor = if (listOf(
            ProfileFeature.profileScreen, ProfileFeature.profileEditScreen
        ).contains(currentRoute)
    ) ColorPurple500 else Color(0xFFFFFBFE)

    val window = (LocalView.current.context as Activity).window
    window.statusBarColor = statusBarColor.toArgb()

    WindowCompat.getInsetsController(
        window, LocalView.current
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(), bottomBar = {
        if (showBottomBar && currentRoute != null) {
            BottomNavigationBar(
                navHostController, currentRoute, basePlayerViewModel = basePlayerViewModel
            )
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            AppNavGraph(
                navController = navHostController,
                navigationProvider = navigationProvider,
                tokenManager = tokenManager
            )
        }
    }
}