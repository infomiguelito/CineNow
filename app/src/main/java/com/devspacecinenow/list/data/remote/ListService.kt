package com.devspacecinenow.list.data.remote

import com.devspacecinenow.common.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface ListService {
    @GET("now_playing?language=en-US&page=1")
    suspend fun getPlayingMovie() : Response<MovieResponse>

    @GET("top_rated?language=en-US&page=1")
    suspend fun getTopRatedMovie() : Response<MovieResponse>

    @GET("upcoming?language=en-US&page=1")
    suspend fun getUpcomingMovies() : Response<MovieResponse>

    @GET("popular?language=en-US&page=1")
    suspend fun getPopularMovies() : Response<MovieResponse>


}