package com.example.thesis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.BookInfoScreen
import com.example.thesis.ui.theme.ThesisTheme
import com.example.ReadEpubBook
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.IOException
import java.io.InputStream
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThesisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var title by remember { mutableStateOf("") }
                    var author by remember { mutableStateOf("") }
                    var text by remember { mutableStateOf("") }
                    var coverImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

                    LaunchedEffect(Unit) {
                        val epubFileName = "testbook.epub"
                        val assetManager = assets

                        ReadEpubBook.readEpubFromAssets(assetManager, epubFileName) { bookTitle, bookAuthor, bookText, coverImage ->
                            title = bookTitle
                            author = bookAuthor
                            text = bookText
                            coverImageBitmap = coverImage
                        }
                    }
                    BookInfoScreen(title = title, author = author, text = text, coverImageBitmap = coverImageBitmap)
                }
            }
        }
    }
}
