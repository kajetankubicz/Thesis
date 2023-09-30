package com.example.thesis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Nawigacja(
    val trasa: String,
    val tytul: String,
    val ikona: ImageVector
){
    object Ksiazki: Nawigacja(
        trasa = "ksiazki",
        tytul = "Książki",
        ikona = Icons.Default.Home
    )
    object Informacje: Nawigacja(
        trasa = "informacje",
        tytul = "Informacje",
        ikona = Icons.Default.Info
    )
}
