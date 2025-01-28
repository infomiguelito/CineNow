package com.devspacecinenow.datail.data

import com.devspacecinenow.common.model.MovieDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetail {
    @GET("{movie_id}?language=en-US")
    suspend fun getMovieById(@Path("movie_id")movieId: String) : Response <MovieDto>
}