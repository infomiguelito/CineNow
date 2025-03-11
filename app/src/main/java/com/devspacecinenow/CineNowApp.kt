package com.devspacecinenow

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devspacecinenow.detail.presentation.MovieDetailViewModel
import com.devspacecinenow.detail.presentation.ui.MovieDetailScreen
import com.devspacecinenow.list.presentation.MovieListViewModel
import com.devspacecinenow.list.presentation.ui.MovieListScreen
import com.devspacecinenow.preferences.presentation.UserPreferencesViewModel
import com.devspacecinenow.preferences.presentation.ui.UserPreferencesScreen

@Composable
fun CineNowApp(
    listViewModel: MovieListViewModel,
    detailViewModel: MovieDetailViewModel,
    preferencesViewModel: UserPreferencesViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movieList") {
        composable(route = "movieList") {
            MovieListScreen(
                navController = navController,
                viewModel = listViewModel
            )
        }
        
        composable(
            route = "movieDetail/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val movieId = requireNotNull(backStackEntry.arguments?.getString("itemId"))
            MovieDetailScreen(movieId, navController, detailViewModel)
        }

        composable(route = "preferences") {
            UserPreferencesScreen(
                navController = navController,
                viewModel = preferencesViewModel,
                movieListViewModel = listViewModel
            )
        }
    }
}