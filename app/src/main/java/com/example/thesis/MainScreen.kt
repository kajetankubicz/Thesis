package com.example.thesis

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thesis.BookManager.addedBooks
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import java.io.IOException
import java.io.InputStream


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: LastViewedPage.BookDetailsViewModel){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            if (currentDestination != "BookDetailsScreen/{title}/{content}") {
                BottomBar(navController = navController)
            }
        }
    ) {
        NavigationGraph(navController = navController, context, addedBooks, viewModel)
    }
}

fun readEpubFromInputStream(inputStream: InputStream): BookInfo {
    val bookTitle: String
    val bookPlainText: String
    var coverImageBitmap: Bitmap? = null

    try {
        val book = EpubReader().readEpub(inputStream)

        bookTitle = book.title ?: "Nieznany Tytuł"
        val content = StringBuilder()

        for (resource in book.contents) {
            content.append(resource.reader.readText())
        }

        bookPlainText = Jsoup.parse(content.toString()).text()

        for (resource in book.resources.all) {
            if (resource.mediaType?.toString()?.startsWith("image/") == true) {
                val coverStream = resource.inputStream
                coverImageBitmap = BitmapFactory.decodeStream(coverStream)
                coverStream.close()
                break
            }
        }

        inputStream.close()

    } catch (e: IOException) {
        e.printStackTrace()
        throw e
    }

    return BookInfo(bookTitle, bookPlainText, coverImageBitmap)
}


@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        Navigation.Books,
        Navigation.Informations
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        screens.forEach { screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Navigation,
    currentDestination: String?,
    navController: NavHostController
) {
    val isSelected = currentDestination == screen.path

    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navController.navigate(screen.path) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
            Box(
                modifier = Modifier.clickable(
                    onClick = {
                        navController.navigate(screen.path) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = "Navigation Icon",
                    tint = if (isSelected) Color(0xFFEC5452) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        label = {
            Text(text = screen.title)
        },
        colors =  NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )
}