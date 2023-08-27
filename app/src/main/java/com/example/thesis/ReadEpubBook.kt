package com.example.thesis

import android.content.res.AssetManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.siegmann.epublib.epub.EpubReader
import java.io.IOException
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import org.jsoup.Jsoup
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController

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
            val content = StringBuilder()
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
                content.append(resource.reader.readText())
            }

            // Close the input stream when done
            epubInputStream.close()

            // Remove formatting tags
            val plainText = Jsoup.parse(content.toString()).text()

            // Pass the book's title, author and content to the callback
            onBookInfoReady(title, author, plainText, coverImageBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun BookInfoScreen(
    title: String,
    author: String,
    content: String,
    coverImageBitmap: Bitmap?
) {

    Surface {
        Column {
            if (coverImageBitmap != null) {
                Image(
                    bitmap = coverImageBitmap.asImageBitmap(),
                    contentDescription = "Book cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp)
                )
            }
            Text(
                text = "Title: $title",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    content = {
                        item {
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                )
        }
    }
}

@Composable
fun BookCoverItem(
    coverImageBitmap: Bitmap?,
    onClick: () -> Unit,
    ) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .fillMaxSize()
            .size(240.dp),
    ) {
        if (coverImageBitmap != null) {
            Image(
                bitmap = coverImageBitmap.asImageBitmap(),
                contentDescription = "Okładka książki",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
