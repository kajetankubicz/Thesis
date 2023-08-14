package com.example.thesis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val context = LocalContext.current
    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        NavigationGraph(navController, context)
    }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        Navigation.Home,
        Navigation.Favourites,
        Navigation.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar{
        screens.forEach{ screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Navigation,
    currentDestination: NavDestination?,
    navController: NavHostController
){
   NavigationBarItem(
       label = {
           Text(text = screen.title)
       },
       icon = {
           Icon(imageVector = screen.icon,
               contentDescription = "Navigation Icon")
       },
       selected = currentDestination?.hierarchy?.any{
           it.route == screen.route
       } == true,
       onClick = {
           navController.navigate(screen.route){
               popUpTo(navController.graph.findStartDestination().id)
               launchSingleTop = true
           }
       }
   )
}