package com.example.thesis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(
    val path: String,
    val title: String,
    val icon: ImageVector
){
    object Books: Navigation(
        path = "books",
        title = "Books",
        icon = Icons.Default.Home
    )
    object Informations: Navigation(
        path = "informations",
        title = "Informations",
        icon = Icons.Default.Info
    )
}
