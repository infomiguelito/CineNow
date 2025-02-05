package com.devspacecinenow.detail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspacecinenow.common.data.RetrofitClient
import com.devspacecinenow.common.model.MovieDto
import com.devspacecinenow.detail.data.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val detailService: MovieDetail
) : ViewModel() {

    private val _uiDetailMovies = MutableStateFlow<MovieDto?>(null)
    val uiDetailMovies: StateFlow<MovieDto?> = _uiDetailMovies

    fun fetchDetailMovies(movieId: String) {
        if (_uiDetailMovies.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = detailService.getMovieById(movieId)
                if (response.isSuccessful) {
                    _uiDetailMovies.value = response.body()
                } else {
                    Log.d(
                        "MovieDetailViewModel",
                        "Request Error :: ${response.errorBody()}"
                    )
                }
            }
        }
    }

    fun cleanMovieId(){
        viewModelScope.launch {
            delay(1000)
            _uiDetailMovies.value = null
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val detailService = RetrofitClient.retrofitInstance.create(MovieDetail::class.java)
                return MovieDetailViewModel(
                    detailService
                ) as T
            }

        }
    }
}





