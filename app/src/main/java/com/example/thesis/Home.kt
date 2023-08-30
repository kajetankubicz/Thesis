package com.example.thesis

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import com.example.thesis.Navigation.Home.title

data class BookInfo(
    val title: String,
    val content: String,
    val coverImageBitmap: Bitmap?,
)

object BookManager {
    val favoriteBooks = mutableStateListOf<BookInfo>()
    fun saveFavorites(context: Context) {
        val prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoriteTitles = favoriteBooks.map { it.title }.toSet()
        prefs.edit {
            putStringSet("favoriteTitles", favoriteTitles)
        }
    }
    fun loadFavorites(context: Context, allBooks: List<BookInfo>) {
        val prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoriteTitles = prefs.getStringSet("favoriteTitles", emptySet())
        favoriteBooks.clear()
        favoriteTitles?.forEach { title ->
            val book = allBooks.find { it.title == title }
            book?.let {
                favoriteBooks.add(it)
            }
        }
    }
}

@Composable
fun BookCoverItem(
    coverImageBitmap: Bitmap?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = onClick)
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.large,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (coverImageBitmap != null) {
                    Image(
                        bitmap = coverImageBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = MaterialTheme.colorScheme.onSurface.copy(0.8f), shape = CircleShape)
                            .clickable {
                                onFavoriteClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red.copy(0.75f) else Color.Gray.copy(0.9f),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    favoriteBooks: MutableList<BookInfo>,
    launcher: ActivityResultLauncher<String>
) {
    val context = LocalContext.current
    val assetManager = LocalContext.current.assets
    //var books by remember { mutableStateOf(favoriteBooks.toMutableList()) }
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

        BookManager.loadFavorites(context, bookList)
    }

    val (favoriteBooksList, nonFavoriteBooksList) = books.partition {
        BookManager.favoriteBooks.contains(it)
    }

    val sortedBooks = favoriteBooksList + nonFavoriteBooksList


    LazyVerticalGrid(
        modifier = Modifier
            .padding(bottom = 80.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        columns = GridCells.Fixed(2),
        content = {
            items(sortedBooks) { book ->
                BookCoverItem(
                    coverImageBitmap = book.coverImageBitmap,
                    isFavorite = BookManager.favoriteBooks.contains(book),
                    onClick = {
                        navController.navigate("BookDetailsScreen/${book.title}/${Uri.encode(book.content)}") {
                            launchSingleTop = true
                        }
                    },
                    onFavoriteClick = {
                        if (BookManager.favoriteBooks.contains(book)) {
                            BookManager.favoriteBooks.remove(book)
                        } else {
                            BookManager.favoriteBooks.add(book)
                        }
                        BookManager.saveFavorites(context)
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






