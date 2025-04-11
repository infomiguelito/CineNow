package com.devspacecinenow.list.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.common.data.remote.model.MovieDto
import com.devspacecinenow.list.presentation.MovieListViewModel

@Composable
fun MovieListScreen(
    navController: NavHostController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val nowPlayingMovies by viewModel.uiNowPlaying.collectAsState()
    val topRatedMovies by viewModel.uiTopRated.collectAsState()
    val upComingMovies by viewModel.uiUpComing.collectAsState()
    val popularMovies by viewModel.uiPopular.collectAsState()
    val recommendedMovies by viewModel.uiRecommended.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CineNow",
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            IconButton(onClick = { navController.navigate("preferences") }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Preferências",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        MovieListContent(
            nowPlayingMovies = nowPlayingMovies,
            topRatedMovies = topRatedMovies,
            popularMovies = popularMovies,
            upComingMovies = upComingMovies,
            recommendedMovies = recommendedMovies
        ) { itemClicked ->
            navController.navigate(route = "movieDetail/${itemClicked.id}")
        }
    }
}

@Composable
private fun MovieListContent(
    nowPlayingMovies: MovieListUiState,
    topRatedMovies: MovieListUiState,
    popularMovies: MovieListUiState,
    upComingMovies: MovieListUiState,
    recommendedMovies: MovieListUiState,
    onClick: (MovieUiData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MovieSession(
            label = "Recommended for You",
            movieListUiState = recommendedMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Top Rated",
            movieListUiState = topRatedMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Now Playing",
            movieListUiState = nowPlayingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Upcoming",
            movieListUiState = upComingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Popular",
            movieListUiState = popularMovies,
            onClick = onClick
        )
    }
}

@Composable
private fun MovieSession(
    label: String,
    movieListUiState: MovieListUiState,
    onClick: (MovieUiData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            fontSize = 24.sp,
            text = label,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.size(8.dp))
        if (movieListUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp)
            )
        } else if (movieListUiState.isError) {
            Text(
                color = Color.Red,
                text = movieListUiState.errorMessage ?:"",
            )
        } else {
            MovieList(movieList = movieListUiState.list, onClick = onClick)
        }
    }
}

@Composable
private fun MovieList(
    movieList: List<MovieUiData>,
    onClick: (MovieUiData) -> Unit
) {
    LazyRow {
        items(movieList) {
            MovieItem(
                movieDto = it,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun MovieItem(
    movieDto: MovieUiData,
    onClick: (MovieUiData) -> Unit
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable {
                onClick.invoke(movieDto)
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 4.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = movieDto.image,
            contentDescription = "${movieDto.title} Poster Image"
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            text = movieDto.title
        )
        if (movieDto.genres.isNotEmpty()) {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                text = movieDto.genres.joinToString(", ")
            )
        }
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = movieDto.overview
        )
    }
}

