package com.example.thesis
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Navigation.Home.route
    ) {
        composable(route = Navigation.Home.route){
            HomeScreen()
        }
        composable(route = Navigation.Favourites.route){
            FavouritesScreen()
        }
        composable(route = Navigation.Settings.route){
            SettingsScreen()
        }
    }
}