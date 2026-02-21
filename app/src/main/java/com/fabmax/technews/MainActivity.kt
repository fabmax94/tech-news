package com.fabmax.technews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RssFeed
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fabmax.technews.presentation.navigation.Screen
import com.fabmax.technews.presentation.navigation.TechNewsNavHost
import com.fabmax.technews.presentation.ui.theme.TechNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechNewsTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val bottomNavItems = listOf(
                    BottomNavItem(
                        screen = Screen.Home,
                        label = "Início",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavItem(
                        screen = Screen.Bookmarks,
                        label = "Salvos",
                        selectedIcon = Icons.Filled.Bookmarks,
                        unselectedIcon = Icons.Outlined.Bookmarks
                    ),
                    BottomNavItem(
                        screen = Screen.Feed,
                        label = "Fontes",
                        selectedIcon = Icons.Filled.RssFeed,
                        unselectedIcon = Icons.Outlined.RssFeed
                    )
                )

                val showBottomBar = currentDestination?.route?.startsWith("article/") != true

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                bottomNavItems.forEach { item ->
                                    val isSelected = currentDestination?.hierarchy?.any {
                                        it.route == item.screen.route
                                    } == true

                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon
                                                              else item.unselectedIcon,
                                                contentDescription = item.label
                                            )
                                        },
                                        label = { Text(item.label) },
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(item.screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
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
                    }
                ) { innerPadding ->
                    TechNewsNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
