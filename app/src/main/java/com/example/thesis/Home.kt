package com.example.thesis

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


/*@Composable
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
}*/

data class BookInfo(val title: String, val content: String, val coverImageBitmap: Bitmap?)

@Composable
fun HomeScreen(navController: NavHostController) {
    val assetManager = LocalContext.current.assets
    var books by remember { mutableStateOf(emptyList<BookInfo>()) }

    LaunchedEffect(Unit) {
        val bookList = mutableListOf<BookInfo>()
        val assetFiles = assetManager.list("") ?: emptyArray()
        val epubFiles = assetFiles.filter { it.endsWith(".epub") }


        epubFiles.forEach { epubFileName ->
            ReadEpubBook.readEpubFromAssets(assetManager, epubFileName) { title, _, plainText, coverImageBitmap ->
                bookList.add(BookInfo(title, plainText, coverImageBitmap))
            }
        }

        books = bookList
    }

    LazyVerticalGrid(
        modifier = Modifier.padding(top = 20.dp, bottom = 100.dp).fillMaxSize(),
        columns = GridCells.Fixed(2),
        content = {
            items(books) { book ->
                BookCoverItem(
                    coverImageBitmap = book.coverImageBitmap,
                    onClick = {
                        navController.navigate("blank_screen/${book.title}/${Uri.encode(book.content)}") {
                            launchSingleTop = true
                        }
                    }
                )
            }

        }
    )
}






