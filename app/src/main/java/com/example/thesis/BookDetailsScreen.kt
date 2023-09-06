package com.example.thesis

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

object LastViewedPage {
    class BookDetailsViewModel : ViewModel() {
        var currentPage: Int = 0
    }
    fun saveLastViewedPage(context: Context, bookIdentifier: String, pageIndex: Int) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("lastViewedPage_$bookIdentifier", pageIndex)
        editor.apply()
    }

    fun getLastViewedPage(context: Context, bookIdentifier: String): Int {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("lastViewedPage_$bookIdentifier", 0) // 0 is the default value if the key is not found
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookDetailsScreen(
    title: String,
    content: String,
    onNavigateBack: () -> Unit,
    navController: NavHostController
) {
    val viewModel: LastViewedPage.BookDetailsViewModel = viewModel()
    val context = LocalContext.current

    val pages = splitContentIntoPages(content)
    val bookIdentifier = title
    val savedPageIndex = LastViewedPage.getLastViewedPage(context, bookIdentifier)

    val pagerState = rememberPagerState(
        initialPage = savedPageIndex,
        initialPageOffsetFraction = 0f
    ) {
        pages.size
    }

    DisposableEffect(pagerState.currentPage) {
        onDispose {
            viewModel.currentPage = pagerState.currentPage
            LastViewedPage.saveLastViewedPage(context, bookIdentifier, pagerState.currentPage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
                ),
                title =  {
                    Text(text = title, color = MaterialTheme.colorScheme.onSurface)
                },
                navigationIcon = {
                    IconButton(onClick = {onNavigateBack()}) {
                        Icon(Icons.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onSurface, contentDescription = "Arrow back")
                    }
                },
                actions = {
                    IconButton(onClick = {navController.navigate("BooksScreen")}) {
                        Icon(Icons.Filled.MoreVert, tint = MaterialTheme.colorScheme.onSurface, contentDescription = "More vert")
                    }
                }
            )
        },
        content = {
            HorizontalPager(
                state  = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val pageContent = pages[page]

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding()),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(
                            text = pageContent,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.open_dyslexic3_bold)),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal // Możesz dostosować wagę czcionki
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    )
}

private fun splitContentIntoPages(content: String): List<String> {
    val maxWordsPerPage = 100
    val words = content.split(Regex("\\s+"))
    val pages = mutableListOf<String>()

    var currentPage = StringBuilder()
    var currentWordCount = 0

    for (word in words) {
        if (currentWordCount + word.split(' ').size <= maxWordsPerPage) {
            currentPage.append("$word ")
            currentWordCount += word.split(' ').size
        } else {
            pages.add(currentPage.toString().trim())
            currentPage = StringBuilder("$word ")
            currentWordCount = word.split(' ').size
        }
    }

    if (currentPage.isNotBlank()) {
        pages.add(currentPage.toString().trim())
    }

    return pages
}