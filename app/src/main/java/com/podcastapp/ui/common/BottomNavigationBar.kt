package com.podcastapp.ui.common

import androidx.compose.material3.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.core.common.constants.MainFeature
import com.core.common.constants.ProfileFeature

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, MainFeature.mainScreenRoute),
        BottomNavItem("Profile", Icons.Default.Person, ProfileFeature.profileScreen),
    )

    BottomAppBar(modifier = modifier) {
        items.forEach { item ->
            IconButton(
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route)
                    }
                }
            ) {
                Icon(imageVector = item.icon, contentDescription = item.title)
                //Text(text = item.title)
            }
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
