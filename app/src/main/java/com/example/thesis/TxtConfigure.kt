package com.example.thesis

import android.content.Context
import android.support.v4.os.IResultReceiver2.Default
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.text.font.Typeface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxtConfigure(
    onNavigateBack: () -> Unit,
    viewModel: LastViewedPage.BookDetailsViewModel
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val fontFamilies = mapOf(
        "Arial_th" to FontFamily(Font(R.font.arial_th)),
        "Open_dyslexic3_bold" to FontFamily(Font(R.font.open_dyslexic3_bold)),
        "Helvetica" to FontFamily(Font(R.font.helvetica)),
        "Verdana" to FontFamily(Font(R.font.verdana)),
        "Courier" to FontFamily(Font(R.font.courier)),
    )

    val selectedFontText = remember {
        mutableStateOf("Arial_th")
    }

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
                    onClick = { isExpanded = !isExpanded },
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

        if (isExpanded) {
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
                                viewModel.selectedFontFamily = fontFamily
                                selectedFontText.value = fontName
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}



