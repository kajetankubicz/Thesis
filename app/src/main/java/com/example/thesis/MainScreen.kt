package com.example.thesis

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

object BookManager {
    val favoriteBooks = mutableStateListOf<BookInfo>()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val favoriteBooks = remember { mutableStateListOf<BookInfo>()}

    Scaffold(
        bottomBar = {
            if (currentDestination != "blank_screen") {
                BottomBar(navController = navController)
            }
        }
    ) {
        NavigationGraph(navController = navController, context, favoriteBooks)
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
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar {
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
    val isSelected = currentDestination == screen.route
    val iconTint = when (screen) {
        Navigation.Home -> if (isSelected) Color.Red else Color.Unspecified
        Navigation.Favourites -> if (isSelected) Color.Blue else Color.Unspecified
        Navigation.Settings -> if (isSelected) Color.Green else Color.Unspecified
    }

    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Box(
                modifier = Modifier.clickable(
                    onClick = {
                        navController.navigate(screen.route) {
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
                    tint = iconTint
                )
            }
        },
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}