package com.devspacecinenow.datail.data

import com.devspacecinenow.common.model.MovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetail {
    @GET("{movie_id}?language=en-US")
    fun getMovieById(@Path("movie_id")movieId: String) : Call<MovieDto>
}