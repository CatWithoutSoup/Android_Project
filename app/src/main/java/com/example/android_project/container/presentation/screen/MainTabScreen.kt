package com.example.android_project.container.presentation.screen

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
import com.example.android_project.viewmodel.FavoriteViewModel
import com.example.android_project.viewmodel.MainViewModel
import com.example.android_project.viewmodel.ProfileViewModel


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
    val profileViewModel: ProfileViewModel = viewModel()
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
        composable(BottomNavItem.Favorite.route) {
            onVisibilityChanged(true)
            val favoriteViewModel: FavoriteViewModel = viewModel()
            FavoriteScreen(
                navController = navController,
                viewModel = favoriteViewModel
            )
        }
        composable(BottomNavItem.Profile.route) {
            onVisibilityChanged(true)
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }

        composable("editProfile") {
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
            onVisibilityChanged(true)
        }

        composable(
            route = "detail/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
            onVisibilityChanged(false)
            DetailScreen(
                cardId = cardId,
                navController = navController,

                )
        }
    }
}
