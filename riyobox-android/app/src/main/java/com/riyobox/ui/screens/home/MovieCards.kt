package com.riyobox.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.riyobox.data.model.Movie

@Composable
fun FeaturedMovieCard(movie: Movie, onClick: () -> Unit) {
    Text("Featured: ${movie.title}")
}

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Text(movie.title)
}
