package com.riyobox.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riyobox.data.model.Movie
import com.riyobox.ui.viewmodel.MovieViewModel

@Composable
fun HomeScreen(
    onMovieClick: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Load movies on first launch
    LaunchedEffect(Unit) {
        if (movies.isEmpty() && !isLoading) {
            viewModel.loadMovies(refresh = true)
        }
    }
    
    // Handle errors
    LaunchedEffect(error) {
        if (error != null) {
            errorMessage = error
            showErrorDialog = true
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RIYOBOX") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading && movies.isEmpty()) {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading movies...")
                }
            }
        } else if (movies.isEmpty() && !isLoading) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No movies available")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadMovies(refresh = true) }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            // Success state
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Featured section
                item {
                    val featured = movies.firstOrNull { it.isFeatured }
                    if (featured != null) {
                        FeaturedMovieCard(
                            movie = featured,
                            onClick = { onMovieClick(featured.id) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                // All movies
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
                
                // Load more item
                item {
                    if (!isLoading && !viewModel.isLastPage) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { viewModel.loadMovies() },
                                enabled = !isLoading
                            ) {
                                Text("Load More")
                            }
                        }
                    } else if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    
    // Error dialog
    if (showErrorDialog && errorMessage != null) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
