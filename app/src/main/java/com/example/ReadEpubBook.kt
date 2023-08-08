package com.example

import android.content.res.AssetManager
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.siegmann.epublib.epub.EpubReader
import java.io.IOException
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.util.CollectionUtil.first
import org.jsoup.Jsoup
import java.io.InputStream

object ReadEpubBook {
    fun readEpubFromAssets(assetManager: AssetManager, epubFileName: String?, onBookInfoReady: (String, String, String) -> Unit) {
        try {
            // Load the EPUB file from assets
            val epubInputStream = assetManager.open(epubFileName!!)

            // Read the EPUB book using EpubReader
            val book = EpubReader().readEpub(epubInputStream)

            // Get the book's title and author
            val title = book.title
            val author = book.metadata.authors.joinToString(", ")
            val text = StringBuilder()

            // Get book content
            for (resource in book.contents) {
                text.append(resource.reader.readText())
            }

            // Close the input stream when done
            epubInputStream.close()

            // Remove formatting tags
            val plainText = Jsoup.parse(text.toString()).text()

            // Pass the book's title, author and content to the callback
            onBookInfoReady(title, author, plainText)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun BookInfoScreen(title: String, author: String, text: String) {
    Surface {
        Column {
            Text(
                text = "Tytuł: " + title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Autorzy: " + author,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}