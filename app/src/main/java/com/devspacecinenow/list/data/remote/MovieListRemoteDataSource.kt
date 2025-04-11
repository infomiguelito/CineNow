package com.devspacecinenow.list.data.remote

import android.accounts.NetworkErrorException
import com.devspacecinenow.common.data.local.MovieCategory
import com.devspacecinenow.common.data.model.Movie
import javax.inject.Inject

class MovieListRemoteDataSource @Inject constructor(
    private val listService: ListService
) : RemoteDataSource {

    override suspend fun getNowPlaying(): Result<List<Movie>?> {
        return try {
            val response = listService.getPlayingMovie()
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map {
                    Movie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        image = it.posterFullPath,
                        category = MovieCategory.NowPlaying.name
                    )
                }
                Result.success(movies)
            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override suspend fun getTopRated(): Result<List<Movie>?> {
        return try {
            val response = listService.getTopRatedMovie()
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map {
                    Movie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        image = it.posterFullPath,
                        category = MovieCategory.TopRated.name
                    )
                }
                Result.success(movies)

            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override suspend fun getPopular(): Result<List<Movie>?> {
        return try {
            val response = listService.getPopularMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map {
                    Movie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        image = it.posterFullPath,
                        category = MovieCategory.Popular.name,
                        genres = it.genre_ids.map { genreId -> getGenreName(genreId) }
                    )
                }
                Result.success(movies)
            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override fun getGenreName(genreId: Int): String {
        return when (genreId) {
            28 -> "Ação"
            12 -> "Aventura"
            16 -> "Animação"
            35 -> "Comédia"
            80 -> "Crime"
            99 -> "Documentário"
            18 -> "Drama"
            10751 -> "Família"
            14 -> "Fantasia"
            36 -> "História"
            27 -> "Terror"
            10402 -> "Música"
            9648 -> "Mistério"
            10749 -> "Romance"
            878 -> "Ficção Científica"
            10770 -> "Cinema TV"
            53 -> "Thriller"
            10752 -> "Guerra"
            37 -> "Faroeste"
            else -> "Outro"
        }
    }

    override suspend fun getUpComing(): Result<List<Movie>?> {
        return try {
            val response = listService.getUpcomingMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map {
                    Movie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        image = it.posterFullPath,
                        category = MovieCategory.Upcoming.name
                    )
                }
                Result.success(movies)
            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}