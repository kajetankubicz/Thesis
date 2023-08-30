package com.example.thesis

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
fun BookCoverItem(
    coverImageBitmap: Bitmap?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (coverImageBitmap != null) {
                Image(
                    bitmap = coverImageBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(200.dp).height(270.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                            .clickable(onClick = onFavoriteClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, favoriteBooks: MutableList<BookInfo>, launcher: ActivityResultLauncher<String>) {
    val assetManager = LocalContext.current.assets
    var books by remember { mutableStateOf(emptyList<BookInfo>()) }
    val addedBooks = remember { mutableStateListOf<BookInfo>() }

    val savedBooks = remember {
        mutableStateOf(
            BookManager.favoriteBooks.toList()
        )
    }
    val homeScreenFavoriteBooks = remember { mutableStateOf<List<BookInfo>>(emptyList()) }

    LaunchedEffect(Unit, favoriteBooks, homeScreenFavoriteBooks, addedBooks) {
        val bookList = mutableListOf<BookInfo>()
        bookList.addAll(savedBooks.value)
        bookList.addAll(favoriteBooks)
        bookList.addAll(homeScreenFavoriteBooks.value)
        bookList.addAll(addedBooks)
        val assetFiles = assetManager.list("") ?: emptyArray()
        val epubFiles = assetFiles.filter { it.endsWith(".epub") }

        epubFiles.forEach { epubFileName ->
            ReadEpubBook.readEpubFromAssets(assetManager, epubFileName) { title, _, plainText, coverImageBitmap ->
                bookList.add(BookInfo(title, plainText, coverImageBitmap))
            }
        }
        //bookList.addAll(savedBooks.value)
        books = bookList
    }

    LazyVerticalGrid(
        modifier = Modifier.padding(top = 20.dp, bottom = 100.dp).fillMaxSize(),
        columns = GridCells.Fixed(2),
        content = {
            items(books) { book ->
                BookCoverItem(
                    coverImageBitmap = book.coverImageBitmap,
                    isFavorite = BookManager.favoriteBooks.contains(book),
                    onClick = {
                        navController.navigate("blank_screen/${book.title}/${Uri.encode(book.content)}") {
                            launchSingleTop = true
                        }
                    },
                    onFavoriteClick = {
                        if (BookManager.favoriteBooks.contains(book)) {
                            BookManager.favoriteBooks.remove(book)
                        } else {
                            BookManager.favoriteBooks.add(book)
                        }
                    }
                )
            }
            item {
                Button(
                    onClick = {
                        launcher.launch("application/epub+zip") // Change the MIME type as needed
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Add Book")
                }
            }
        }
    )
}






