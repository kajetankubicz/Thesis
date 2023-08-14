package com.example.thesis

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Composable
fun SettingsScreen(context: Context) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBook: Book? by remember { mutableStateOf(null) }
    val openDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        selectedFileUri = uri
        selectedBook = uri?.let { readEpubFile(context, it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column {
            Button(onClick = { openDocumentLauncher.launch(arrayOf("application/epub+zip")) }) {
                Text(text = "Add EPUB Book")
            }

            selectedBook?.let { book ->
                LazyColumn {
                    item {
                        Text(text = "Title: ${book.title}", modifier = Modifier.padding(16.dp))
                        Text(text = "Author: ${book.metadata.authors.joinToString { it.toString() }}", modifier = Modifier.padding(16.dp))
                        Text(text = "Number of chapters: ${book.spine.size()}", modifier = Modifier.padding(16.dp))
                    }

                    items(book.spine.size()) { chapterIndex ->
                        val resource = book.spine.getResource(chapterIndex)
                        val chapterContent = resource?.reader?.readText() ?: "Chapter content not available"

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Chapter $chapterIndex",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(text=Jsoup.parse(chapterContent).text(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

private fun readEpubFile(context: Context, uri: Uri): Book? {
    val inputStream = context.contentResolver.openInputStream(uri)
    return try {
        inputStream?.let {
            val epubReader = EpubReader()
            epubReader.readEpub(it)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}

private fun chapterContentsToString(chapter: Resource): String {
    val inputStream = chapter.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
    val stringBuilder = StringBuilder()

    var line: String? = reader.readLine()
    while (line != null) {
        stringBuilder.append(line)
        line = reader.readLine()
    }

    reader.close()
    inputStream.close()

    return stringBuilder.toString()
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(text = "Sets")
    }
}