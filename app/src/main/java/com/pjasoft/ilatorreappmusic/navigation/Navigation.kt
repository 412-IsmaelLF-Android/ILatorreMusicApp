package com.pjasoft.ilatorreappmusic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pjasoft.ilatorreappmusic.ui.detail.DetailScreen
import com.pjasoft.ilatorreappmusic.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{albumId}") {
        fun createRoute(albumId: String) = "detail/$albumId"
    }
}

@Composable
fun MusicNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Detail.createRoute(albumId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("albumId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            DetailScreen(
                albumId = albumId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
