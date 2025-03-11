package com.devspacecinenow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.devspacecinenow.detail.presentation.MovieDetailViewModel
import com.devspacecinenow.list.presentation.MovieListViewModel
import com.devspacecinenow.preferences.data.UserPreferencesRepository
import com.devspacecinenow.preferences.presentation.UserPreferencesViewModel
import com.devspacecinenow.ui.theme.CineNowTheme

class MainActivity : ComponentActivity() {

    private val listViewModel by viewModels<MovieListViewModel> {MovieListViewModel.factory}
    private val detailViewModel by viewModels<MovieDetailViewModel> {MovieDetailViewModel.factory}
    private val preferencesViewModel by viewModels<UserPreferencesViewModel> {
        UserPreferencesViewModel.factory(UserPreferencesRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CineNowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CineNowApp(
                        listViewModel = listViewModel,
                        detailViewModel = detailViewModel,
                        preferencesViewModel = preferencesViewModel
                    )
                }
            }
        }
    }
}




