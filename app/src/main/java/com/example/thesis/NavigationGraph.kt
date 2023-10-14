package com.example.thesis

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationGraph(navController: NavHostController, context: Context, dodaneKsiazki: MutableList<BookInfo>, viewModel: OstatniaStrona.BookDetailsViewModel){
    val backStackEntry = navController.currentBackStackEntryAsState()
    val previousRoute = backStackEntry.value?.destination?.route
    NavHost(
        navController = navController,
        startDestination = Nawigacja.Ksiazki.trasa

    ) {
        composable(route = Nawigacja.Ksiazki.trasa){
            EkranKsiazek(navController, dodaneKsiazki)
        }
        composable(route = Nawigacja.Informacje.trasa){
            EkranInformacji(context)
        }
        composable(
            route = "BookDetailsScreen/{title}/{content}") { backStackEntry ->
            val tytul = backStackEntry.arguments?.getString("title") ?: ""
            val tesc = Uri.decode(backStackEntry.arguments?.getString("content") ?: "")
            val letterSpacingEnabled = backStackEntry.arguments?.getString("letterSpacingEnabled")?.toBoolean() ?: false
            BookDetailsScreen(tytul, tesc, { navController.popBackStack() }, navController, viewModel, letterSpacingEnabled)
        }
        composable(route = "BooksScreen") {
            TxtConfigure(
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}

