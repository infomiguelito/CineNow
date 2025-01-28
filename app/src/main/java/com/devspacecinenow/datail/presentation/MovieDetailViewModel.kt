package com.devspacecinenow.datail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspacecinenow.common.data.RetrofitClient
import com.devspacecinenow.common.model.MovieDto
import com.devspacecinenow.datail.data.MovieDetail
import com.devspacecinenow.list.data.ListService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(
   private val detailService: MovieDetail
) : ViewModel(){

    private val _uiDetailMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val uiDetailMovies: StateFlow<List<MovieDto>> = _uiDetailMovies


    init {

    }

    private fun fetchDetailMovies(){
        detailService.getMovieById(movieId).enqueue(
            object : Callback<MovieDto> {
                override fun onResponse(call: Call<MovieDto>, response: Response<MovieDto>) {
                    if (response.isSuccessful) {
                        _uiDetailMovies =
                    } else {
                        Log.d("MovieDetailViewModel", "Request Error :: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<MovieDto>, t: Throwable) {
                    Log.d("MovieDetailViewModel", "Network Error :: ${t.message}")
                }
            }
        )
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val detailService = RetrofitClient.retrofitInstance.create(ListService::class.java)
                return MovieDetailViewModel(
                    detailService
                ) as T
            }

        }
    }
}


