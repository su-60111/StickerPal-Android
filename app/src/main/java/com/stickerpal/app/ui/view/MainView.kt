package com.stickerpal.app.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stickerpal.app.ui.view.home.HomeScreen
import com.stickerpal.app.ui.view.habits.HabitScreen
import com.stickerpal.app.ui.view.habits.TimerScreen
import com.stickerpal.app.ui.view.pet.PetScreen
import com.stickerpal.app.ui.view.stickers.StickerScreen
import com.stickerpal.app.ui.view.stickers.StickerDetailScreen
import com.stickerpal.app.ui.view.settings.SettingsScreen
import com.stickerpal.app.viewmodel.HabitViewModel
import com.stickerpal.app.viewmodel.HomeViewModel
import com.stickerpal.app.viewmodel.PetViewModel
import com.stickerpal.app.viewmodel.StickerViewModel

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun MainView(
    homeViewModel: HomeViewModel,
    habitViewModel: HabitViewModel,
    stickerViewModel: StickerViewModel,
    petViewModel: PetViewModel
) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavItem("首页", Icons.Default.Home, "home"),
        BottomNavItem("习惯", Icons.Default.Book, "habits"),
        BottomNavItem("贴纸", Icons.Default.Collections, "stickers"),
        BottomNavItem("设置", Icons.Default.Settings, "settings"),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(viewModel = homeViewModel, habitViewModel = habitViewModel) }
            composable("habits") { HabitScreen(onStartTimer = { habitViewModel.setShowTimer(true); navController.navigate("timer") }, viewModel = habitViewModel) }
            composable("timer") { TimerScreen(viewModel = habitViewModel, onBack = { navController.popBackStack() }) }
            composable("stickers") { StickerScreen(viewModel = stickerViewModel, onNavigateToDetail = { stickerId -> navController.navigate("sticker_detail/$stickerId") }) }
            composable("sticker_detail/{stickerId}") { backStackEntry ->
                val stickerId = backStackEntry.arguments?.getString("stickerId") ?: ""
                StickerDetailScreen(stickerId = stickerId, viewModel = stickerViewModel, onBack = { navController.popBackStack() })
            }
            composable("pet") { PetScreen(viewModel = petViewModel, homeViewModel = homeViewModel) }
            composable("settings") { SettingsScreen(petViewModel = petViewModel, homeViewModel = homeViewModel) }
        }
    }
}
