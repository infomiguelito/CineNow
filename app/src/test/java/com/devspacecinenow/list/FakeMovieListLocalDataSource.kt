package com.devspacecinenow.list

import com.devspacecinenow.common.data.model.Movie
import com.devspacecinenow.list.data.local.LocalDataSource

class FakeMovieListLocalDataSource: LocalDataSource {

    var nowPlaying = emptyList<Movie>()
    var topRated = emptyList<Movie>()
    var Popupar = emptyList<Movie>()
    var upComing = emptyList<Movie>()
    var updateItems = emptyList<Movie>()

    override suspend fun getNowPlayingMovies(): List<Movie> {
        return nowPlaying
    }

    override suspend fun getTopRatedMovies(): List<Movie> {
        return topRated
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return Popupar
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return upComing
    }

    override suspend fun updateLocalItems(movies: List<Movie>) {
        updateItems = movies
    }
}