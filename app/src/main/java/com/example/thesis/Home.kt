package com.example.thesis

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun HomeScreen() {
    val assetManager = LocalContext.current.assets
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        var title by remember { mutableStateOf(" ") }
        var author by remember { mutableStateOf(" ") }
        var content by remember { mutableStateOf(" ") }
        var coverImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(Unit) {
            val epubFileName = "testbook.epub"

            ReadEpubBook.readEpubFromAssets(assetManager, epubFileName) { bookTitle, bookAuthor, bookText, coverImage ->
                title = bookTitle
                author = bookAuthor
                content = bookText
                coverImageBitmap = coverImage
            }
        }
        BookInfoScreen(title = title, author = author, content = content, coverImageBitmap = coverImageBitmap)
    }
}


