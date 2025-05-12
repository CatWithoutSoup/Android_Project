package com.example.android_project.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("MainTabScreen", "Домой", Icons.Default.Home)
    object List : BottomNavItem("ListScreen", "Список", Icons.AutoMirrored.Filled.List)
    object Profile : BottomNavItem("ProfileScreen", "Профиль", Icons.Default.Person)

    companion object {
        val items = listOf(Home, List, Profile)
    }
}