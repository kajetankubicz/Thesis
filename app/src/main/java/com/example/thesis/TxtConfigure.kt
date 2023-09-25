package com.example.thesis

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxtConfigure(
    onNavigateBack: () -> Unit,
    viewModel: LastViewedPage.BookDetailsViewModel
) {
    var isFontExpanded by remember { mutableStateOf(false) }
    var isSizeExpanded by remember { mutableStateOf(false) }

    val fontFamilies = mapOf(
        "Arial_th" to FontFamily(Font(R.font.arial_th)),
        "Open_dyslexic3_bold" to FontFamily(Font(R.font.open_dyslexic3_bold)),
        "Helvetica" to FontFamily(Font(R.font.helvetica)),
        "Verdana" to FontFamily(Font(R.font.verdana)),
        "Courier" to FontFamily(Font(R.font.courier)),
    )

    //var selectedFontSize by remember { mutableStateOf(14) }
    val selectedFontText = remember { mutableStateOf("Arial_th") }

    Column {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
            ),
            title = {
                Text(text = "Ustawienia tekstu", color = MaterialTheme.colorScheme.onSurface)
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Arrow back"
                    )
                }
            },
        )

        OutlinedTextField(
            value = selectedFontText.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { isFontExpanded = !isFontExpanded },
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Dropdown",
                    )
                }
            },
            label = { Text(text = "Czcionka") },
            colors = TextFieldDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isFontExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    fontFamilies.forEach { (fontName, fontFamily) ->
                        DropdownMenuItem(
                            text = {
                                Text(text = fontName)
                            },
                            onClick = {
                                BookManager.selectedFontFamily = fontFamily
                                selectedFontText.value = fontName
                                isFontExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = BookManager.selectedFontSize.toString(),
            onValueChange = {
                val newSize = it.toIntOrNull()
                if (newSize != null) {
                    BookManager.selectedFontSize = newSize.sp
                }
            },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { isSizeExpanded = !isSizeExpanded },
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Dropdown",
                    )
                }
            },
            label = { Text(text = "Rozmiar czcionki") },
            colors = TextFieldDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isSizeExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    val fontSizes = listOf(14, 16, 18, 20, 22, 24, 26)
                    fontSizes.forEach { fontSize ->
                        DropdownMenuItem(
                            text = {
                                Text(text = fontSize.toString())
                            },
                            onClick = {
                                BookManager.selectedFontSize = fontSize.sp
                                isSizeExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}





