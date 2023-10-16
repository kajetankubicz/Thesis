package com.example.thesis

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

object LastViewedPage {
    class BookDetailsViewModel : ViewModel() {
        var currentPage: Int = 0
        var chooseFontFamily: FontFamily = FontFamily(Font(R.font.open_dyslexic3_bold))
    }
    fun saveLastPage(context: Context, title: String, pageIndex: Int) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("OstatniaStrona_$title", pageIndex)
        editor.apply()
    }

    fun getLastPage(context: Context, title: String): Int {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("OstatniaStrona_$title", 0)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookDetailsScreen(
    title: String,
    content: String,
    onNavigateBack: () -> Unit,
    navController: NavHostController,
    viewModel: LastViewedPage.BookDetailsViewModel,
    letterSpacingEnabled: Boolean,
    highlightSimilarLetters: Boolean,
    bgColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val viewModel: LastViewedPage.BookDetailsViewModel = viewModel()
    val context = LocalContext.current
    var chooseFontSize  by mutableStateOf(BookManager.chooseFontSize)

    val pages = splitContentIntoPages(content, chooseFontSize.value.toInt(), screenHeight, screenWidth, letterSpacingEnabled)
    val savedPageIndex = LastViewedPage.getLastPage(context, title)

    val pagerState = rememberPagerState(
        initialPage = savedPageIndex,
        initialPageOffsetFraction = 0f
    ) {
        pages.size
    }

    DisposableEffect(pagerState.currentPage) {
        onDispose {
            viewModel.currentPage = pagerState.currentPage
            LastViewedPage.saveLastPage(context, title, pagerState.currentPage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
                ),
                title = {
                    Text(text = title, color = MaterialTheme.colorScheme.onSurface)
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Arrow back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("BooksScreen") }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "More vert"
                        )
                    }
                }
            )
        },
        content = {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val pageContent = pages[page]

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding())
                        .background(BookManager.chooseBgColor ?: bgColor),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        val letterSpacing = if (BookManager.letterSpacingEnabled) 0.2.em else 0.em

                        CustomText(
                            text = pageContent,
                            fontFamily = BookManager.chooseFontFamily  ?: FontFamily.Default,
                            fontSize = BookManager.chooseFontSize,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = letterSpacing,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            highlightLetters = highlightSimilarLetters,
                            modifier = Modifier.fillMaxWidth(1f),
                            keyboard = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
                        )
                    }
                }
            }
        }
    )
}

private fun splitContentIntoPages(content: String, fontSize: Int, screenHeight: Dp, screenWidth: Dp, letterSpacing: Boolean): List<String> {
    val words = content.split(Regex("\\s+"))
    val pages = mutableListOf<String>()

    var currentPage = StringBuilder()
    var currentWordCount = 0

    for (word in words) {
        val wordsInCurrentWord = word.split(' ').size

        val availableWidth = screenWidth - (16.dp)
        val charsPerLine = if (letterSpacing) {
            (availableWidth / (fontSize + 32)).value.toInt()
        } else {
            (availableWidth / fontSize).value.toInt()
        }
        val maxLines = (screenHeight / fontSize).value.toInt()

        val maxWordsPerPage = (charsPerLine * maxLines) / (fontSize / 2)

        if (currentWordCount + wordsInCurrentWord <= maxWordsPerPage) {
            currentPage.append("$word ")
            currentWordCount += wordsInCurrentWord
        } else {
            pages.add(currentPage.toString().trim())
            currentPage = StringBuilder("$word ")
            currentWordCount = wordsInCurrentWord
        }
    }

    if (currentPage.isNotBlank()) {
        pages.add(currentPage.toString().trim())
    }

    return pages
}