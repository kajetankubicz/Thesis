package com.example.thesis
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, favoriteBooks: MutableList<BookInfo>){
    NavHost(
        navController = navController,
        startDestination = Navigation.Home.route

    ) {
        composable(route = Navigation.Home.route){
            HomeScreen(navController, favoriteBooks)
        }
        composable(route = Navigation.Favourites.route){
            FavouritesScreen(navController) { book ->
                BookManager.favoriteBooks.remove(book)
            }
        }
        composable(route = Navigation.Settings.route){
            SettingsScreen(context)
        }
        composable(route = "blank_screen/{title}/{content}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val content = Uri.decode(backStackEntry.arguments?.getString("content") ?: "")
            BlankScreen(title ?: "", content, navController::popBackStack)
        }
    }
}

