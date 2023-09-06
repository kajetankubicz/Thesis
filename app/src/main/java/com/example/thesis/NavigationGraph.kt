package com.example.thesis

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, favoriteBooks: MutableList<BookInfo>){
    val backStackEntry = navController.currentBackStackEntryAsState()
    val previousRoute = backStackEntry.value?.destination?.route
    NavHost(
        navController = navController,
        startDestination = Navigation.Home.route

    ) {
        composable(route = Navigation.Home.route){
            HomeScreen(navController, favoriteBooks)
        }
        composable(route = Navigation.Settings.route){
            SettingsScreen(context)
        }
        composable(
            route = "BookDetailsScreen/{title}/{content}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val content = Uri.decode(backStackEntry.arguments?.getString("content") ?: "")
            BookDetailsScreen(title ?: "", content, navController::popBackStack, navController)
        }
        composable(route = "BooksScreen") {
            TxtConfigure(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

