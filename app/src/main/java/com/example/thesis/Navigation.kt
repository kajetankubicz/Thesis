package com.example.thesis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Home: Navigation(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Favourites: Navigation(
        route = "favourites",
        title = "Favourites",
        icon = Icons.Default.Star
    )
    object Settings: Navigation(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )

}
