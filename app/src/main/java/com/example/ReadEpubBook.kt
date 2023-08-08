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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap


object ReadEpubBook {
    fun readEpubFromAssets(assetManager: AssetManager, epubFileName: String?, onBookInfoReady: (String, String, String, Bitmap?) -> Unit) {
        try {
            // Load the EPUB file from assets
            val epubInputStream = assetManager.open(epubFileName!!)

            // Read the EPUB book using EpubReader
            val book = EpubReader().readEpub(epubInputStream)

            // Get the book's title and author
            val title = book.title
            val author = book.metadata.authors.joinToString(", ")
            val text = StringBuilder()
            var coverImageBitmap: Bitmap? = null

            // Book cover
            for (resource in book.resources.all) {
                if (resource.mediaType?.toString()?.startsWith("image/") == true) {
                    val coverStream = resource.inputStream
                    coverImageBitmap = BitmapFactory.decodeStream(coverStream)
                    coverStream.close()
                    break
                }
            }

            // Get book content
            for (resource in book.contents) {
                text.append(resource.reader.readText())
            }

            // Close the input stream when done
            epubInputStream.close()

            // Remove formatting tags
            val plainText = Jsoup.parse(text.toString()).text()

            // Pass the book's title, author and content to the callback
            onBookInfoReady(title, author, plainText, coverImageBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun BookInfoScreen(title: String, author: String, text: String, coverImageBitmap: Bitmap?) {
    Surface {
        Column {
            if (coverImageBitmap != null) {
                Image(
                    bitmap = coverImageBitmap.asImageBitmap(),
                    contentDescription = "Okładka książki",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp)
                )
            }
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