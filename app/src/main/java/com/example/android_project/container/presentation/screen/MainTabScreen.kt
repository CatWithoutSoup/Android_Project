package com.example.android_project.container.presentation.screen

import DetailScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_project.BottomNavBar
import com.example.android_project.model.BottomNavItem
import com.example.android_project.viewmodel.MainViewModel


@Composable
fun MainTabScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    var bottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (bottomBarVisible) {
                BottomNavBar(navController = navController, viewModel = viewModel)
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onVisibilityChanged = { isVisible -> bottomBarVisible = isVisible }
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            onVisibilityChanged(true)
            HomeScreen()
        }
        composable(BottomNavItem.List.route) {
            onVisibilityChanged(true)
            ListScreen(navController)
        }
        composable(BottomNavItem.Profile.route) {
            onVisibilityChanged(true)
            Screen3()
        }
        composable(
            route = "detail/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: return@composable
            onVisibilityChanged(false)
            DetailScreen(
                index = index,
                navController = navController
            )
        }
    }
}
