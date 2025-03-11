package com.devspacecinenow.list.data

import com.devspacecinenow.common.data.model.Movie
import com.devspacecinenow.list.data.local.LocalDataSource
import com.devspacecinenow.list.data.remote.MovieListRemoteDataSource
import com.devspacecinenow.preferences.data.UserPreferencesRepository

class MovieListRepository(
    private val local: LocalDataSource,
    private val remote: MovieListRemoteDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun getNowPlaying(): Result<List<Movie>?> {
        return try {
            val result = remote.getNowPlaying()
            if (result.isSuccess) {
                val moviesRemote = result.getOrNull() ?: emptyList()
                if (moviesRemote.isNotEmpty()) {
                    local.updateLocalItems(moviesRemote)
                }
                return Result.success(local.getNowPlayingMovies())
            } else {
                val localData = local.getNowPlayingMovies()
                if (localData.isEmpty()) {
                    return result
                } else {
                    Result.success(localData)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun getTopRated(): Result<List<Movie>?> {
        return try {
            val result = remote.getTopRated()
            if (result.isSuccess) {
                val moviesRemote = result.getOrNull() ?: emptyList()
                if (moviesRemote.isNotEmpty()) {
                    local.updateLocalItems(moviesRemote)
                }
                return Result.success(local.getTopRatedMovies())
            } else {
                val localData = local.getTopRatedMovies()
                if (localData.isEmpty()) {
                    return result
                } else {
                    Result.success(localData)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun getPopular(): Result<List<Movie>?> {
        return try {
            val result = remote.getPopular()
            if (result.isSuccess) {
                val moviesRemote = result.getOrNull() ?: emptyList()
                if (moviesRemote.isNotEmpty()) {
                    local.updateLocalItems(moviesRemote)
                }
                return Result.success(local.getPopularMovies())
            } else {
                val localData = local.getPopularMovies()
                if (localData.isEmpty()) {
                    return result
                } else {
                    Result.success(localData)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun getUpComing(): Result<List<Movie>?> {
        return try {
            val result = remote.getUpComing()
            if (result.isSuccess) {
                val moviesRemote = result.getOrNull() ?: emptyList()
                if (moviesRemote.isNotEmpty()) {
                    local.updateLocalItems(moviesRemote)
                }
                return Result.success(local.getUpcomingMovies())
            } else {
                val localData = local.getUpcomingMovies()
                if (localData.isEmpty()) {
                    return result
                } else {
                    Result.success(localData)
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun getRecommended(): Result<List<Movie>?> {
        return try {
            // Primeiro tenta buscar do remoto
            val result = remote.getPopular()
            
            // Lista de filmes que vamos usar (remoto ou local)
            val movies = if (result.isSuccess) {
                val moviesRemote = result.getOrNull() ?: emptyList()
                if (moviesRemote.isNotEmpty()) {
                    local.updateLocalItems(moviesRemote)
                }
                moviesRemote
            } else {
                // Se falhar o remoto, usa o local
                local.getPopularMovies()
            }

            println("DEBUG: Filmes obtidos: ${movies.size}")
            
            if (movies.isEmpty()) {
                println("DEBUG: Nenhum filme disponível")
                return Result.failure(Exception("Nenhum filme disponível"))
            }

            val selectedGenres = userPreferencesRepository.getSelectedGenres().first()
            println("DEBUG: Gêneros selecionados: $selectedGenres")
            
            val watchedMovies = userPreferencesRepository.getWatchedMovies()
            println("DEBUG: Filmes assistidos: $watchedMovies")
            
            // Filmes que não foram assistidos
            val unwatchedMovies = movies.filterNot { movie -> movie.id in watchedMovies }
            println("DEBUG: Filmes não assistidos: ${unwatchedMovies.size}")
            
            // Se não há filmes não assistidos, retorna os filmes originais
            if (unwatchedMovies.isEmpty()) {
                println("DEBUG: Retornando filmes originais (todos assistidos)")
                return Result.success(movies.take(10))
            }
            
            val recommendedMovies = if (selectedGenres.isEmpty()) {
                // Se não há gêneros selecionados, retorna os filmes não assistidos
                println("DEBUG: Nenhum gênero selecionado, retornando filmes não assistidos")
                unwatchedMovies.take(10)
            } else {
                // Filtrar filmes que tenham pelo menos um dos gêneros selecionados
                val matchingMovies = unwatchedMovies.filter { movie ->
                    val hasMatchingGenre = movie.genres.any { it in selectedGenres }
                    println("DEBUG: Filme ${movie.title} tem gêneros ${movie.genres}, match: $hasMatchingGenre")
                    hasMatchingGenre
                }
                
                // Se não encontrar filmes com os gêneros selecionados, retorna filmes não assistidos
                if (matchingMovies.isEmpty()) {
                    println("DEBUG: Nenhum filme com gêneros selecionados, retornando filmes não assistidos")
                    unwatchedMovies.take(10)
                } else {
                    // Ordenar por quantidade de gêneros em comum
                    println("DEBUG: Encontrados ${matchingMovies.size} filmes com gêneros selecionados")
                    matchingMovies.sortedByDescending { movie ->
                        movie.genres.count { it in selectedGenres }
                    }.take(10)
                }
            }
            
            // Garantir que sempre retornamos uma lista não vazia
            if (recommendedMovies.isEmpty()) {
                println("DEBUG: Lista de recomendações vazia, retornando filmes originais")
                Result.success(movies.take(10))
            } else {
                println("DEBUG: Retornando ${recommendedMovies.size} filmes recomendados")
                Result.success(recommendedMovies)
            }
        } catch (ex: Exception) {
            println("DEBUG: Exceção: ${ex.message}")
            ex.printStackTrace()
            // Tenta retornar filmes do cache local em caso de erro
            val localMovies = local.getPopularMovies()
            return if (localMovies.isNotEmpty()) {
                println("DEBUG: Retornando ${localMovies.size} filmes do cache local")
                Result.success(localMovies.take(10))
            } else {
                Result.failure(ex)
            }
        }
    }
}