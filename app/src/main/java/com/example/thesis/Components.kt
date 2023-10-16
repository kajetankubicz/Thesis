package com.example.thesis

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit = {},
    fontFamily: FontFamily = FontFamily.SansSerif,
    fontSize: TextUnit,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit,
    textColor: androidx.compose.ui.graphics.Color,
    highlightLetters: Boolean = false,
    onImeAction: () -> Unit = {},
    keyboard: KeyboardOptions,
) {

    val letterColor = if (highlightLetters) MaterialTheme.colorScheme.error else textColor
    val keyboardController = LocalSoftwareKeyboardController.current


    if (highlightLetters) {
        Text(
            buildAnnotatedString {
                for (char in text) {
                    withStyle(style = SpanStyle(color = if (char in "pbgdwv") MaterialTheme.colorScheme.error else textColor)) {
                        append(char.toString())
                    }
                }
            },
            style = TextStyle(
                fontFamily = fontFamily,
                fontSize = fontSize,
                fontWeight = fontWeight,
                letterSpacing = letterSpacing,
            ),
            color = letterColor,
        )
    }else{
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontSize = fontSize,
                fontWeight = fontWeight,
                letterSpacing = letterSpacing,
                color = textColor,
            ),
            modifier = modifier,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            keyboardActions = KeyboardActions(onAny = {
                keyboardController?.hide()
            }),
            readOnly = true,
            enabled = false
        )
    }
}

