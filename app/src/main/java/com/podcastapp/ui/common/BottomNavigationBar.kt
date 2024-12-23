package com.podcastapp.ui.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    iconSize: Dp = 48.dp
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, MainFeature.mainScreenRoute),
        BottomNavItem("Profile", Icons.Default.Person, ProfileFeature.profileScreen),
    )

    BottomAppBar(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape((iconSize * 2.5f)))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true, radius = (iconSize * 2.5f))
                        ) {
                            if (currentDestination != item.route) {
                                Log.d("TAG", "go to " + item.route)
                                navController.navigate(item.route)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
