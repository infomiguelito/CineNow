package com.cinenow

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRecommendationExample(private val context: Context) {
    private val recommender = MovieRecommender(context)
    private val scope = CoroutineScope(Dispatchers.Main)
    
    fun getMovieRecommendations() {
        // Exemplo de filmes que o usuário já assistiu
        val watchedMovies = listOf(
            MovieRecommender.MovieFeatures(
                id = 1,
                genres = listOf("Ação", "Aventura"),
                rating = 4.5f,
                releaseYear = 2023,
                popularity = 8.7f
            ),
            MovieRecommender.MovieFeatures(
                id = 2,
                genres = listOf("Comédia", "Romance"),
                rating = 4.0f,
                releaseYear = 2022,
                popularity = 7.5f
            )
        )
        
        // Exemplo de preferências do usuário
        val userPreferences = mapOf(
            "Ação" to 0.8f,
            "Aventura" to 0.7f,
            "Comédia" to 0.6f,
            "Drama" to 0.4f,
            "Ficção Científica" to 0.9f
        )
        
        scope.launch {
            try {
                val recommendations = recommender.getRecommendations(watchedMovies, userPreferences)
                // Aqui você pode usar os IDs dos filmes recomendados para buscar mais informações
                // na sua API de filmes e exibir para o usuário
                println("Filmes recomendados: $recommendations")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
} 