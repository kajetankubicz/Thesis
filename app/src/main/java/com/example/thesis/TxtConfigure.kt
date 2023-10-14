package com.example.thesis

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxtConfigure(
    onNavigateBack: () -> Unit,
    viewModel: OstatniaStrona.BookDetailsViewModel
) {
    var isFontExpanded by remember { mutableStateOf(false) }
    var isSizeExpanded by remember { mutableStateOf(false) }
    var letterSpacingEnabled by remember { mutableStateOf(false) }
    var highlightSimilarLetters by remember { mutableStateOf(false) }
    var isBackgroundColorExpanded by remember { mutableStateOf(false) }

    val fontFamilies = mapOf(
        "Arial_th" to FontFamily(Font(R.font.arial_th)),
        "Open_dyslexic3_bold" to FontFamily(Font(R.font.open_dyslexic3_bold)),
        "Helvetica" to FontFamily(Font(R.font.helvetica)),
        "Verdana" to FontFamily(Font(R.font.verdana)),
        "Courier" to FontFamily(Font(R.font.courier)),
    )

    val selectedFontText = remember { mutableStateOf("Arial_th") }
    var selectedBackgroundColor = remember { mutableStateOf("d5a6bd") }

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
                                BookManager.wybranaRodzinaCzcionki = fontFamily
                                selectedFontText.value = fontName
                                isFontExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = selectedBackgroundColor.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { isBackgroundColorExpanded = !isBackgroundColorExpanded },
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Background Color Dropdown",
                    )
                }
            },
            label = { Text(text = "Kolor tła ") },
            colors = TextFieldDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isBackgroundColorExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    val backgroundColors = listOf(
                        "0xf44336" to Color(0xf44336),
                        "0xf1c232" to Color(0xf1c232),
                        "0x6aa84f" to Color(0x6aa84f),
                        "0xd5a6bd" to Color(0xd5a6bd)
                    )
                    backgroundColors.forEach { (color) ->
                        DropdownMenuItem(
                            text = {
                                Text(text = color)
                            },
                            onClick = {
                                selectedBackgroundColor.value = color
                                isBackgroundColorExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = BookManager.wybranyRozmiarCzcionki.toString(),
            onValueChange = {
                val newSize = it.toIntOrNull()
                if (newSize != null) {
                    BookManager.wybranyRozmiarCzcionki = newSize.sp
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
                                BookManager.wybranyRozmiarCzcionki = fontSize.sp
                                isSizeExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Przerwy między literami", modifier = Modifier.weight(1f))
            Switch(
                checked = BookManager.letterSpacingEnabled,
                onCheckedChange = { newCheckedValue ->
                    BookManager.letterSpacingEnabled = newCheckedValue
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Wyróżnij podobne litery", modifier = Modifier.weight(1f))
            Switch(
                checked = BookManager.highlightSimilarLetters,
                onCheckedChange = { newCheckedValue ->
                    BookManager.highlightSimilarLetters = newCheckedValue
                }
            )
        }
    }
}

@Composable
fun HighlightSimilarLettersText(
    text: String,
    highlightSimilarLetters: Set<Char>
) {
    val similarLetters = setOf('p', 'b', 'g', 'd', 'w', 'v')
    val textWithSpannable = buildAnnotatedString {
        text.forEach { char ->
            val textColor = if (similarLetters.contains(char) && char in highlightSimilarLetters) {
                Color.Red
            } else {
                Color.Black
            }
            withStyle(style = SpanStyle(color = textColor)) {
                append(char.toString())
            }
        }
    }
    Text(textWithSpannable)
}





