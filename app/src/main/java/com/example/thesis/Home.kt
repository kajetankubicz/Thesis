package com.example.thesis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

data class BookInfo(
    val title: String,
    val content: String,
    val coverImageBitmap: Bitmap?,
    var isFavorite: Boolean = false
)

object BookManager {
    val dodaneKsiazki = mutableStateListOf<BookInfo>()
    var wybranyRozmiarCzcionki by mutableStateOf(20.sp)
    var wybranaRodzinaCzcionki by mutableStateOf<FontFamily?>(null)

    fun toggleFavorite(bookInfo: BookInfo, context: Context) {
        bookInfo.isFavorite = !bookInfo.isFavorite
        saveAdded(context)
    }
    fun saveAdded(context: Context) {
        val prefs = context.getSharedPreferences("Added", Context.MODE_PRIVATE)
        val favoriteBooksJson = dodaneKsiazki.map { bookInfo ->
            val bookData = mutableMapOf<String, Any>()
            bookData["title"] = bookInfo.title
            bookData["content"] = bookInfo.content
            bookData["isFavorite"] = bookInfo.isFavorite
            if (bookInfo.coverImageBitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bookInfo.coverImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
                bookData["coverImageBitmap"] = encodedImage
            }
            return@map bookData
        }
        val json = Gson().toJson(favoriteBooksJson)
        prefs.edit {
            putString("favoriteBooksJson", json)
        }
    }
    fun loadAdded(context: Context, allBooks: List<BookInfo>) {
        val prefs = context.getSharedPreferences("Added", Context.MODE_PRIVATE)
        val json = prefs.getString("favoriteBooksJson", null)
        dodaneKsiazki.clear()

        if (!json.isNullOrEmpty()) {
            val favoriteBooksJson = Gson().fromJson<List<Map<String, Any>>>(json, object : TypeToken<List<Map<String, Any>>>() {}.type)
            favoriteBooksJson.forEach { bookData ->
                val title = bookData["title"] as? String ?: ""
                val content = bookData["content"] as? String ?: ""
                val encodedImage = bookData["coverImageBitmap"] as? String
                val coverImageBitmap = if (!encodedImage.isNullOrEmpty()) {
                    val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } else {
                    null
                }
                val isFavorite = bookData["isFavorite"] as? Boolean ?: false
                val bookInfo = allBooks.find { it.title == title && it.content == content }?.apply {
                    this.isFavorite = isFavorite
                } ?: BookInfo(title, content, coverImageBitmap, isFavorite)
                dodaneKsiazki.add(bookInfo)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookCoverItem(
    coverImageBitmap: Bitmap?,
    bookInfo: BookInfo,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    val isLongPressed = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    isLongPressed.value = true
                }
            )
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
                            .background(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                shape = CircleShape
                            )
                            .clickable {
                                BookManager.toggleFavorite(bookInfo, context)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (bookInfo.isFavorite) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (bookInfo.isFavorite) Color.Red.copy(0.75f) else Color.Gray.copy(0.9f),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }
    if (isLongPressed.value) {
        AlertDialog(
            onDismissRequest = { isLongPressed.value = false },
            title = { Text(text = "Usunąć książkę?") },
            text = {
                Text(text = "Czy na pewno chcesz usunąć tę książkę?")

            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        isLongPressed.value = false
                    },
                ) {
                    Text(text = "Tak")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        isLongPressed.value = false
                    },
                ) {
                    Text(text = "Nie")
                }
            }
        )
    }
}

@Composable
fun EkranKsiazek(
    navController: NavHostController,
    dodaneKsiazki: MutableList<BookInfo>,
) {
    val context = LocalContext.current

    var books by remember { mutableStateOf(emptyList<BookInfo>()) }

    val bookList = mutableListOf<BookInfo>()
    bookList.addAll(BookManager.dodaneKsiazki)
    BookManager.loadAdded(context, bookList)
    books = bookList

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(bottom = 80.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            columns = GridCells.Fixed(2),
            content = {
                items(books) { book ->
                    BookCoverItem(
                        coverImageBitmap = book.coverImageBitmap,
                        bookInfo = book,
                        onClick = {
                            navController.navigate(
                                "BookDetailsScreen/${book.title}/${
                                    Uri.encode(
                                        book.content
                                    )
                                }"
                            ) {
                                launchSingleTop = true
                            }
                        },
                        onDeleteClick = {
                            books = books.filterNot { it == book }
                            BookManager.dodaneKsiazki.remove(book)
                            BookManager.saveAdded(context)
                        }
                    )

                }
            }
        )

        val selectedEpubFile = remember { mutableStateOf<Uri?>(null) }
        val updatedSelectedEpubFile = rememberUpdatedState(selectedEpubFile.value)
        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedEpubFile.value = uri
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
                .padding(16.dp),
            onClick = {
                filePickerLauncher.launch("application/epub+zip")
            },
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(Icons.Filled.Add, "Dodaj ksiazke")

        }

        if (selectedEpubFile.value != null) {
            LaunchedEffect(selectedEpubFile.value) {
                val epubUri = updatedSelectedEpubFile.value
                if (epubUri != null) {
                    val inputStream = context.contentResolver.openInputStream(epubUri)
                    if (inputStream != null) {
                        val newBook = readEpubFromInputStream(inputStream)
                        books = books + newBook
                        BookManager.dodaneKsiazki.add(newBook)
                        BookManager.saveAdded(context)
                        selectedEpubFile.value = null
                    }
                }
            }
        }
    }
}


