package com.riyobox.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.riyobox.ui.screens.auth.LoginScreen
import com.riyobox.ui.screens.auth.RegisterScreen
import com.riyobox.ui.screens.Category.CategoryScreen
import com.riyobox.ui.screens.downloads.DownloadsScreen
import com.riyobox.ui.screens.home.HomeScreen
import com.riyobox.ui.screens.movie.MovieDetailScreen
import com.riyobox.ui.screens.player.VideoPlayerScreen
import com.riyobox.ui.screens.profile.MyRiyoboxScreen
import com.riyobox.ui.screens.search.SearchScreen
import com.riyobox.ui.viewmodel.AuthViewModel
import com.riyobox.ui.viewmodel.MovieDetailViewModel
import com.riyobox.ui.components.BottomNavigationBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isLoggedIn.value) "main" else "login"
    ) {
        // Auth Screens
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("main") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        // Main Navigation (Bottom Navigation)
        composable("main") {
            MainNavigationScreen(
                navController = navController,
                onMovieClick = { movieId ->
                    navController.navigate("movie/$movieId")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        // Movie Detail
        composable("movie/{id}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("id") ?: ""
            MovieDetailScreen(
                movieId = movieId,
                onPlayClick = { movieId ->
                    navController.navigate("player/$movieId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Video Player
        composable("player/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            val viewModel: MovieDetailViewModel = hiltViewModel()
            
            // Load movie details to get video URL
            LaunchedEffect(movieId) {
                viewModel.loadMovie(movieId)
            }
            
            val movie by viewModel.movie.collectAsState()
            
            if (movie != null) {
                VideoPlayerScreen(
                    videoUrl = movie!!.videoUrl,
                    movieTitle = movie!!.title,
                    movieId = movieId,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

// Main Navigation with Bottom Bar
@Composable
fun MainNavigationScreen(
    navController: NavHostController,
    onMovieClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Bottom navigation items
    val screens = listOf("home", "category", "search", "downloads", "profile")

    // Track current screen
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    // Setup bottom navigation
    androidx.compose.material3.Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onItemClick = { route ->
                    if (currentDestination != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    onMovieClick = onMovieClick,
                    onLogout = onLogout
                )
            }

            composable("category") {
                CategoryScreen(
                    onCategoryClick = { categoryId ->
                        // Navigate to category movies
                        navController.navigate("category/$categoryId/movies")
                    },
                    onMovieClick = onMovieClick
                )
            }

            composable("search") {
                SearchScreen(
                    onMovieClick = onMovieClick
                )
            }

            composable("downloads") {
                DownloadsScreen(
                    onMovieClick = onMovieClick
                )
            }

            composable("profile") {
                MyRiyoboxScreen(
                    onLogout = onLogout,
                    onNavigateToFavorites = { 
                        navController.navigate("favorites")
                    },
                    onNavigateToWatchHistory = { 
                        navController.navigate("watch-history")
                    },
                    onNavigateToSettings = { 
                        navController.navigate("settings")
                    }
                )
            }

            // Additional screens for navigation
            composable("category/{id}/movies") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("id") ?: ""
                CategoryMoviesScreen(
                    categoryId = categoryId,
                    onMovieClick = onMovieClick,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("favorites") {
                FavoritesScreen(
                    onMovieClick = onMovieClick,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("watch-history") {
                WatchHistoryScreen(
                    onMovieClick = onMovieClick,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
