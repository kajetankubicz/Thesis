package com.example.thesis

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thesis.BookManager.highlightSimilarLetters

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, addedBooks: MutableList<BookInfo>, viewModel: LastViewedPage.BookDetailsViewModel){
    val backStackEntry = navController.currentBackStackEntryAsState()
    val previousRoute = backStackEntry.value?.destination?.route
    NavHost(
        navController = navController,
        startDestination = Navigation.Books.path

    ) {
        composable(route = Navigation.Books.path){
            BookScreen(navController, addedBooks)
        }
        composable(route = Navigation.Informations.path){
            InfoScreen(context)
        }
        composable(
            route = "BookDetailsScreen/{title}/{content}") { backStackEntry ->
            val title  = backStackEntry.arguments?.getString("title") ?: ""
            val content  = Uri.decode(backStackEntry.arguments?.getString("content") ?: "")
            val letterSpacingEnabled = backStackEntry.arguments?.getString("letterSpacingEnabled")?.toBoolean() ?: false
            BookDetailsScreen(
                title,
                content,
                { navController.popBackStack() },
                navController,
                viewModel,
                letterSpacingEnabled,
                highlightSimilarLetters,
                )
        }
        composable(route = "BooksScreen") {
            TxtConfigure(
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}

