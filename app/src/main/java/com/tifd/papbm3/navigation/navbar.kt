package com.tifd.papbm3.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue

// Data class untuk item navigasi
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screenRoute: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavigationItem("Home", Icons.Default.Home, "home"),
        NavigationItem("Profile", Icons.Default.Person, "profile"),
        NavigationItem("Tugas", Icons.Default.Settings, "settings")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
