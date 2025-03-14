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

            val selectedGenres = userPreferencesRepository.getSelectedGenres()
            println("DEBUG: Gêneros selecionados: $selectedGenres")
            
            val watchedMovies = userPreferencesRepository.getWatchedMovies()
            println("DEBUG: Filmes assistidos: $watchedMovies")
            
            // Filmes que não foram assistidos
            val unwatchedMovies = movies.filterNot { movie -> movie.id in watchedMovies }
            println("DEBUG: Filmes não assistidos: ${unwatchedMovies.size}")
            
            // Se não há filmes não assistidos, retorna os filmes originais
            if (unwatchedMovies.isEmpty()) {
                println("DEBUG: Retornando filmes originais (todos assistidos)")
                return Result.success(movies.take(20))
            }
            
            val recommendedMovies = if (selectedGenres.isEmpty()) {
                // Se não há gêneros selecionados, retorna os filmes não assistidos
                println("DEBUG: Nenhum gênero selecionado, retornando filmes não assistidos")
                unwatchedMovies.take(20)
            } else {
                // Primeiro, vamos contar quantos filmes temos por gênero
                val availableMoviesPerGenre = selectedGenres.associateWith { genre ->
                    unwatchedMovies.count { movie -> movie.genres.contains(genre) }
                }
                println("DEBUG: Filmes disponíveis por gênero: $availableMoviesPerGenre")

                // Filtramos apenas os gêneros que têm filmes disponíveis
                val genresWithMovies = availableMoviesPerGenre.filter { it.value > 0 }.keys.toList()
                println("DEBUG: Gêneros com filmes disponíveis: $genresWithMovies")

                if (genresWithMovies.isEmpty()) {
                    // Se nenhum dos gêneros selecionados tem filmes, retorna filmes não assistidos
                    println("DEBUG: Nenhum filme encontrado para os gêneros selecionados")
                    unwatchedMovies.take(20)
                } else {
                    // Calculamos quantos filmes pegar de cada gênero
                    val moviesPerGenre = (20 / genresWithMovies.size).coerceAtLeast(5)
                    println("DEBUG: Pegando até $moviesPerGenre filmes de cada gênero")

                    // Agrupar filmes por gênero
                    val moviesByGenre = mutableListOf<Movie>()
                    
                    // Para cada gênero que tem filmes, pegamos a quantidade calculada
                    genresWithMovies.forEach { genre ->
                        val moviesForGenre = unwatchedMovies.filter { movie ->
                            movie.genres.contains(genre)
                        }.take(moviesPerGenre)
                        moviesByGenre.addAll(moviesForGenre)
                        println("DEBUG: Adicionados ${moviesForGenre.size} filmes do gênero $genre")
                    }
                    
                    // Remove duplicatas
                    val uniqueMovies = moviesByGenre.distinct()
                    println("DEBUG: Total de filmes únicos: ${uniqueMovies.size}")
                    
                    // Se não tivermos filmes suficientes, adiciona mais dos filmes não assistidos
                    if (uniqueMovies.size < 20) {
                        val remainingCount = 20 - uniqueMovies.size
                        val additionalMovies = unwatchedMovies
                            .filterNot { it in uniqueMovies }
                            .filter { movie ->
                                movie.genres.any { it in genresWithMovies }
                            }
                            .take(remainingCount)
                        
                        println("DEBUG: Adicionando mais ${additionalMovies.size} filmes para completar")
                        (uniqueMovies + additionalMovies).distinct()
                    } else {
                        uniqueMovies.take(20)
                    }
                }
            }
            
            // Garantir que sempre retornamos uma lista não vazia
            if (recommendedMovies.isEmpty()) {
                println("DEBUG: Lista de recomendações vazia, retornando filmes originais")
                Result.success(movies.take(20))
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
                Result.success(localMovies.take(20))
            } else {
                Result.failure(ex)
            }
        }
    }
}