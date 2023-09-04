package com.example.thesis

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Composable
fun FavouritesScreen(
    navController: NavHostController,
) {
    /*val favoriteBooks = BookManager.favoriteBooks


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 20.dp, bottom = 100.dp).fillMaxSize(),
            columns = GridCells.Fixed(2),
            content = {
                items(favoriteBooks) { book ->
                    BookCoverItem(
                        coverImageBitmap = book.coverImageBitmap,
                        isFavorite = true,
                        onClick = {
                            navController.navigate("blank_screen/${book.title}/${Uri.encode(book.content)}") {
                                launchSingleTop = true
                            }
                                  },
                        onFavoriteClick = {
                            BookManager.favoriteBooks.remove(book)
                        }
                    )
                }
            }
        )
    }*/
}
