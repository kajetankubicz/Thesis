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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Composable
fun EkranInformacji(context: Context) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBook: Book? by remember { mutableStateOf(null) }
    val openDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        selectedFileUri = uri
        selectedBook = uri?.let { readEpubFile(context, it) }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.onSurface),
                content = {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Aplikacja czytnika e-booków dla dzieci z dysleksją rozwojową",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.W800,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 0.10.em,
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Opiekun pracy: dr inż. Michał Wróbel", modifier = Modifier.padding(16.dp),fontSize = 25.sp)
            Text(text = "Dyplomanci: Renata Bańka, Kajetan Kubicz", modifier = Modifier.padding(16.dp),fontSize = 25.sp)



           /* selectedBook?.let { book ->
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
            } */
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
