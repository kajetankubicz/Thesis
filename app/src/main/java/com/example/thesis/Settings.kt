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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@OptIn(ExperimentalPagerApi::class)
@Composable
fun InfoScreen(
    navController: NavHostController,
    context: Context
) {
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

            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Praca dymplomowa: Aplikacja czytnika e-booków dla dzieci z dysleksją rozwojową", modifier = Modifier.padding(16.dp),fontSize = 20.sp, fontWeight = FontWeight.W700)

            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Opiekun pracy: dr inż. Michał Wróbel", modifier = Modifier.padding(16.dp),fontSize = 20.sp, fontWeight = FontWeight.W700)

            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Dyplomanci: Renata Bańka, Kajetan Kubicz", modifier = Modifier.padding(16.dp),fontSize = 20.sp, fontWeight = FontWeight.W700)

            Spacer(modifier = Modifier.size(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                onClick = {
                navController.navigate(Navigation.Walkthrough.path)
            }) {
                Text(text = "Samouczek", modifier = Modifier.padding(16.dp),fontSize = 16.sp)
            }

            /*selectedBook?.let { book ->
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
                        Text(text= Jsoup.parse(chapterContent).text(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }*/
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
