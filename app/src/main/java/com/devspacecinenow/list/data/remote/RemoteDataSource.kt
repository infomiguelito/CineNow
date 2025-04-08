package com.devspacecinenow.list.data.remote

import com.devspacecinenow.common.data.model.Movie

interface RemoteDataSource {

    suspend fun getNowPlaying(): Result<List<Movie>?>

    suspend fun getTopRated(): Result<List<Movie>?>

    suspend fun getPopular(): Result<List<Movie>?>

    suspend fun getUpComing(): Result<List<Movie>?>

    fun getGenreName(genreId: Int): String

}