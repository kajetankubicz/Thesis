package com.example.thesis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TxtConfigure(
    onNavigateBack: () -> Unit,
    viewModel: LastViewedPage.BookDetailsViewModel,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFontExpanded by remember { mutableStateOf(false) }
    var isSizeExpanded by remember { mutableStateOf(false) }
    var letterSpacingEnabled by remember { mutableStateOf(false) }
    var highlightSimilarLetters by remember { mutableStateOf(false) }
    var isBackgroundColorExpanded by remember { mutableStateOf(false) }
    var isTextColorExpanded by remember { mutableStateOf(false) }
    var syllableSpacing by remember { mutableStateOf(false) }

    val fontFamilies = mapOf(
        "Arial" to FontFamily(Font(R.font.arial_th)),
        "Open dyslexic" to FontFamily(Font(R.font.open_dyslexic3_bold)),
        "Helvetica" to FontFamily(Font(R.font.helvetica)),
        "Verdana" to FontFamily(Font(R.font.verdana)),
        "Courier" to FontFamily(Font(R.font.courier)),
    )

    val bgColors = mapOf(
        "Czarny" to Color.Black,
        "Biały" to Color.White,
        "Czerwony" to Color(0xFFEC5452),
        "Żółty" to Color(0xFFECC14F),
        "Niebieski" to Color(0xFF52B9EC),
        "Zielony" to Color(0xFF66AE60)
    )

    val txColors = mapOf(
        "Czarny" to Color.Black,
        "Biały" to Color.White,
        "Czerwony" to Color(0xFFB81311),
        "Pomarańczowy" to Color(0xFFFF8A00),
        "Niebieski" to Color(0xFF1468b4),
        "Zielony" to Color(0xFF41921e)
    )

    val selectedFontText = remember { mutableStateOf("Arial") }
    var selectedBackgroundColor = remember { mutableStateOf("Czarny") }
    val selectedTextColor = remember { mutableStateOf("Czarny") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
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

        var isFontFocused by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()

        OutlinedTextField(
            value = selectedFontText.value,
            onValueChange = {},
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            keyboardActions = KeyboardActions(onAny = {
                keyboardController?.hide()
            }),
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
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFontFocused = it.isFocused
                    if (it.isFocused) {
                        keyboardController?.hide()
                    }
                },
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
                                Text(text = fontName, fontSize = 18.sp)
                            },
                            onClick = {
                                BookManager.chooseFontFamily = fontFamily
                                selectedFontText.value = fontName
                                isFontExpanded = false
                            }
                        )
                    }
                }
            }
        }

        var isBgFocused by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = selectedBackgroundColor.value,
            onValueChange = {},
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            keyboardActions = KeyboardActions(onAny = {
                keyboardController?.hide()
            }),
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
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isBgFocused = it.isFocused
                    if (it.isFocused) {
                        keyboardController?.hide()
                    }
                },
        )

        if (isBackgroundColorExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    bgColors.forEach { (colorName, bgColor) ->
                        DropdownMenuItem(
                            text = {
                                Text(text = colorName, fontSize = 18.sp)
                            },
                            onClick = {
                                BookManager.chooseBgColor = bgColor
                                selectedBackgroundColor.value = colorName
                                isBackgroundColorExpanded = false
                            }
                        )
                    }
                }
            }
        }


        var isFontSizeFocused by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = BookManager.chooseFontSize.toString().replace(".0", "").replace(".sp", ""),
            onValueChange = {
                val newSize = it.toIntOrNull()
                if (newSize != null) {
                    BookManager.chooseFontSize = newSize.sp
                }
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            keyboardActions = KeyboardActions(onAny = {
                keyboardController?.hide()
            }),
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
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFontSizeFocused = it.isFocused
                    if (it.isFocused) {
                        keyboardController?.hide()
                    }
                },
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
                                Text(text = fontSize.toString(), fontSize = 18.sp)
                            },
                            onClick = {
                                BookManager.chooseFontSize = fontSize.sp
                                isSizeExpanded = false
                            }
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            value = selectedTextColor.value,
            onValueChange = {},
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            keyboardActions = KeyboardActions(onAny = {
                keyboardController?.hide()
            }),
            trailingIcon = {
                IconButton(
                    onClick = { isTextColorExpanded = !isTextColorExpanded },
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Text Color Dropdown",
                    )
                }
            },
            label = { Text(text = "Kolor tekstu") },
            colors = TextFieldDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isTextColorExpanded = it.isFocused
                    if (it.isFocused) {
                        keyboardController?.hide()
                    }
                },
        )

        if (isTextColorExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    txColors.forEach { (colorName, txColor) ->
                        DropdownMenuItem(
                            text = {
                                Text(text = colorName, fontSize = 18.sp)
                            },
                            onClick = {
                                BookManager.chooseTextColor = txColor
                                selectedTextColor.value = colorName
                                isTextColorExpanded = false
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
            Text(text = "Przerwy między literami", modifier = Modifier.weight(1f), fontSize = 18.sp)
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
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Wyróżnij podobne litery", modifier = Modifier.weight(1f), fontSize = 18.sp)
            Switch(
                checked = BookManager.highlightSimilarLetters,
                onCheckedChange = { newCheckedValue ->
                    BookManager.highlightSimilarLetters = newCheckedValue
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Przerwy między sylabami", modifier = Modifier.weight(1f), fontSize = 18.sp)
            Switch(
                checked = BookManager.syllableSpacing,
                onCheckedChange = { newCheckedValue ->
                    BookManager.syllableSpacing = newCheckedValue
                }
            )
        }
    }
}






