package com.podcastapp.ui.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.core.common.constants.MainFeature
import com.core.common.constants.PlayerFeature
import com.core.common.constants.ProfileFeature
import com.core.common.theme.ColorWhite
import com.podcastapp.ui.navigation.screen.BottomCapedPlayer
import com.podcastapp.ui.navigation.viewmodels.BasePlayerViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: String,
    iconSize: Dp = 48.dp,
    basePlayerViewModel: BasePlayerViewModel
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, MainFeature.mainScreenRoute),
        BottomNavItem(
            "Profile", Icons.Filled.Person, Icons.Outlined.Person, ProfileFeature.profileScreen
        ),
    )

    val height =
        if (basePlayerViewModel.ifContainsEpisode() && currentDestination != PlayerFeature.playerScreen) 160.dp else 80.dp


    BottomAppBar(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(color = Color(0xFFFFFBFE))
        .graphicsLayer {
            shape = RoundedCornerShape(
                topStart = 30.dp, topEnd = 30.dp, bottomEnd = 0.dp, bottomStart = 0.dp
            )
            shadowElevation = 5f
        }
        .height(100.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorWhite)
        ) {
            if (basePlayerViewModel.ifContainsEpisode()) {
                BottomCapedPlayer(basePlayerViewModel, onClick = {
                    navController.navigate(PlayerFeature.playerScreen)
                })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(ColorWhite),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape((iconSize * 2.5f)))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = true, radius = (iconSize * 2.5f)
                                )
                            ) {
                                if (currentDestination != item.route) {
                                    navController.navigate(item.route)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (currentDestination == item.route) item.iconChosen else item.iconDefault,
                            contentDescription = item.title,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val title: String, val iconChosen: ImageVector, val iconDefault: ImageVector, val route: String
)
