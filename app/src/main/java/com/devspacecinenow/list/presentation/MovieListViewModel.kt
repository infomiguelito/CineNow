package com.devspacecinenow.list.presentation

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspacecinenow.common.data.RetrofitClient
import com.devspacecinenow.common.model.MovieDto
import com.devspacecinenow.common.model.MovieResponse
import com.devspacecinenow.list.data.ListService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MovieListViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _uiNowPlaying = MutableStateFlow<List<MovieDto>>(emptyList())
    val uiNowPlaying: StateFlow<List<MovieDto>> = _uiNowPlaying

    private val _uiTopRated = MutableStateFlow<List<MovieDto>>(emptyList())
    val uiTopRated: StateFlow<List<MovieDto>> = _uiTopRated

    private val _uiUpComing = MutableStateFlow<List<MovieDto>>(emptyList())
    val uiUpComing: StateFlow<List<MovieDto>> = _uiUpComing

    private val _uiPopular = MutableStateFlow<List<MovieDto>>(emptyList())
    val uiPopular: StateFlow<List<MovieDto>> = _uiPopular

    init {
        fetchPopularMovies()
        fetchNowPlayingMovies()
        fetchTopRatedMovies()
        fetchUpComingMovies()
    }

    private fun fetchNowPlayingMovies() {
        listService.getPlayingMovie().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>, response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        _uiNowPlaying.value = movies
                    }

                } else {
                    Log.d("MovieListViewModel", "Request Error :: ${response.errorBody()}")
                }

            }


            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("MovieListViewModel", "Network Error :: ${t.message}")
            }

        })
    }

    private fun fetchTopRatedMovies() {
        listService.getTopRatedMovie().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>, response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        _uiTopRated.value = movies
                    }

                } else {
                    Log.d("MovieListViewModel", "Request Error :: ${response.errorBody()}")
                }

            }


            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("MovieListViewModel", "Network Error :: ${t.message}")
            }

        })
    }

    private fun fetchUpComingMovies() {
        listService.getUpcomingMovies().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        _uiUpComing.value = movies
                    }

                } else {
                    Log.d("MovieListViewModel", "Request Error :: ${response.errorBody()}")
                }

            }


            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("MovieListViewModel", "Network Error :: ${t.message}")
            }

        })
    }

    private fun fetchPopularMovies() {
        listService.getPopularMovies().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>, response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        _uiPopular.value = movies
                    }

                } else {
                    Log.d("MovieListViewModel", "Request Error :: ${response.errorBody()}")
                }

            }


            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("MovieListViewModel", "Network Error :: ${t.message}")
            }

        })
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val listService = RetrofitClient.retrofitInstance.create(ListService::class.java)
                return MovieListViewModel(
                    listService
                ) as T
            }

        }
    }
}