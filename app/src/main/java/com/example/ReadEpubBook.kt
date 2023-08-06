package com.example

import android.content.res.AssetManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.siegmann.epublib.epub.EpubReader
import java.io.IOException

object ReadEpubBook {
    fun readEpubFromAssets(assetManager: AssetManager, epubFileName: String?, onBookInfoReady: (String, String) -> Unit) {
        try {
            // Load the EPUB file from assets
            val epubInputStream = assetManager.open(epubFileName!!)

            // Read the EPUB book using EpubReader
            val book = EpubReader().readEpub(epubInputStream)

            // Get the book's title and author
            val title = book.title
            val author = book.metadata.authors.joinToString(", ")

            // Close the input stream when done
            epubInputStream.close()

            // Pass the book's title and author to the callback
            onBookInfoReady(title, author)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun BookInfoScreen(title: String, author: String) {
    Surface {
        Column {
            Text(
                text = "Tytuł: " + title,
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            )
            Text(
                text = "Autorzy: " + author,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            )
        }
    }
}