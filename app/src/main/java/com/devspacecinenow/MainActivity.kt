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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CineNowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CineNowApp(

                    )
                }
            }
        }
    }
}




