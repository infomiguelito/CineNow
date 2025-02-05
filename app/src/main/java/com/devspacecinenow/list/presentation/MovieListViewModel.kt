package com.devspacecinenow.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspacecinenow.CineNowApplication
import com.devspacecinenow.common.data.RetrofitClient
import com.devspacecinenow.list.data.remote.ListService
import com.devspacecinenow.list.data.MovieListRepository
import com.devspacecinenow.list.presentation.ui.MovieListUiState
import com.devspacecinenow.list.presentation.ui.MovieUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MovieListViewModel(
    private val repository: MovieListRepository
) : ViewModel() {

    private val _uiNowPlaying = MutableStateFlow(MovieListUiState())
    val uiNowPlaying: StateFlow<MovieListUiState> = _uiNowPlaying

    private val _uiTopRated = MutableStateFlow(MovieListUiState())
    val uiTopRated: StateFlow<MovieListUiState> = _uiTopRated

    private val _uiUpComing = MutableStateFlow(MovieListUiState())
    val uiUpComing: StateFlow<MovieListUiState> = _uiUpComing

    private val _uiPopular = MutableStateFlow(MovieListUiState())
    val uiPopular: StateFlow<MovieListUiState> = _uiPopular

    init {
        fetchPopularMovies()
        fetchNowPlayingMovies()
        fetchTopRatedMovies()
        fetchUpComingMovies()
    }

    private fun fetchNowPlayingMovies() {
        _uiNowPlaying.value = MovieListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getNowPlaying()
            if (result.isSuccess) {
                val movies = result.getOrNull()?.results
                if (movies != null) {
                    val movieUiDataList = movies.map { movieDto ->
                        MovieUiData(
                            id = movieDto.id,
                            title = movieDto.title,
                            overview = movieDto.overview,
                            image = movieDto.posterFullPath
                        )
                    }
                    _uiNowPlaying.value = MovieListUiState(list = movieUiDataList)
                }
            } else {
                val ex = result.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiNowPlaying.value = MovieListUiState(
                        isError = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiNowPlaying.value = MovieListUiState(isError = true)
                }
            }
        }
    }


    private fun fetchTopRatedMovies() {
        _uiTopRated.value = MovieListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getTopRated()
            if (response.isSuccess) {
                val movies = response.getOrNull()?.results
                if (movies != null) {
                    val movieUiData = movies.map { movieDto ->
                        MovieUiData(
                            id = movieDto.id,
                            title = movieDto.title,
                            overview = movieDto.overview,
                            image = movieDto.posterFullPath
                        )
                    }
                    _uiTopRated.value = MovieListUiState(list = movieUiData)
                }

            } else {
                val ex = response.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiTopRated.value = MovieListUiState(
                        isError = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiTopRated.value = MovieListUiState(isError = true)
                }
            }
        }
    }


    private fun fetchUpComingMovies() {
        _uiUpComing.value = MovieListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getUpComing()
            if (response.isSuccess) {
                val movies = response.getOrNull()?.results
                if (movies != null) {
                    val movieUiData = movies.map { movieDto ->
                        MovieUiData(
                            id = movieDto.id,
                            title = movieDto.title,
                            overview = movieDto.overview,
                            image = movieDto.posterFullPath
                        )
                    }
                    _uiUpComing.value = MovieListUiState(list = movieUiData)
                }
            } else {
                val ex = response.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiUpComing.value = MovieListUiState(
                        isError = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiUpComing.value = MovieListUiState(isError = true)
                }

            }
        }
    }

    private fun fetchPopularMovies() {
        _uiPopular.value = MovieListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPopular()
            if (response.isSuccess) {
                val movies = response.getOrNull()?.results
                if (movies != null) {
                    val movieUiData = movies.map { movieDto ->
                        MovieUiData(
                            id = movieDto.id,
                            title = movieDto.title,
                            overview = movieDto.overview,
                            image = movieDto.posterFullPath
                        )
                    }

                    _uiPopular.value = MovieListUiState(list = movieUiData)
                }
            } else {
                val ex = response.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiPopular.value = MovieListUiState(
                        isError = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiPopular.value = MovieListUiState(isError = true)
                }
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MovieListViewModel(
                    repository = (application as CineNowApplication).repository
                ) as T
            }

        }
    }
}