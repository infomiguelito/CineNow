package com.devspacecinenow.datail.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.common.model.MovieDto
import com.devspacecinenow.common.data.RetrofitClient
import com.devspacecinenow.datail.presentation.MovieDetailViewModel
import com.devspacecinenow.ui.theme.CineNowTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    movieId: String,
    navHostController: NavHostController,
    viewModel: MovieDetailViewModel
) {
    val movieDto by viewModel.uiDetailMovies.collectAsState()
    viewModel.fetchDetailMovies(movieId)


    movieDto?.let {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.cleanMovieId()
                    navHostController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = it.title
                )
            }
            MovieDetailContent(it)
        }
    }
}

@Composable
private fun MovieDetailContent(movie: MovieDto) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = movie.posterFullPath,
            contentDescription = "${movie.title} Poster image"
        )

        Text(
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp),
            text = movie.overview
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailPreview() {
    CineNowTheme {
        val movie = MovieDto(
            id = 8,
            title = "Title",
            postPath = "sfçssf",
            overview = "Long overview movie " +
                    "Long overview movie" +
                    " Long overview movie " +
                    "Long overview movie"
        )
        MovieDetailContent(movie = movie)
    }
}