package com.devspacecinenow.preferences.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devspacecinenow.preferences.presentation.UserPreferencesViewModel
import androidx.navigation.NavHostController
import com.devspacecinenow.list.presentation.MovieListViewModel

@Composable
fun UserPreferencesScreen(
    navController: NavHostController,
    viewModel: UserPreferencesViewModel = hiltViewModel(),
    movieListViewModel: MovieListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Suas Preferências",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Gêneros Favoritos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.availableGenres) { genre ->
                GenreChip(
                    genre = genre,
                    isSelected = genre in uiState.selectedGenres,
                    onSelectionChanged = { selected ->
                        if (selected) {
                            viewModel.addGenre(genre)
                        } else {
                            viewModel.removeGenre(genre)
                        }
                    }
                )
            }
        }

        // Botões de ação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    viewModel.savePreferences {
                        movieListViewModel.updateRecommendations()
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Salvar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreChip(
    genre: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = { onSelectionChanged(!isSelected) },
        label = { Text(genre) },
        modifier = Modifier.fillMaxWidth()
    )
} 