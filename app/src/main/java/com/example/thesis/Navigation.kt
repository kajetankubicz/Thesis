package com.example.thesis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(
    val path: String,
    val title: String,
    val icon: ImageVector
){
    object Books: Navigation(
        path = "books",
        title = "Książki",
        icon = Icons.Default.Home
    )
    object Informations: Navigation(
        path = "informations",
        title = "Informacje",
        icon = Icons.Default.Info
    )
    object Walkthrough: Navigation(
        path = "walkthrough",
        title = "Samouczek",
        icon = Icons.Default.Info
    )
}
