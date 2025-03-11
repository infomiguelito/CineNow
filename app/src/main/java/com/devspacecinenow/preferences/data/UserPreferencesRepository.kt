package com.devspacecinenow.preferences.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Definindo o DataStore no nível do companion object
class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user_preferences"
        )

        private val SELECTED_GENRES = stringPreferencesKey("selected_genres")
        private val WATCHED_MOVIES = stringPreferencesKey("watched_movies")
        private val RATED_MOVIES = stringPreferencesKey("rated_movies")
    }

    suspend fun saveSelectedGenres(genres: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_GENRES] = genres.joinToString(",")
        }
    }

    suspend fun getSelectedGenres(): List<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[SELECTED_GENRES]?.split(",")?.filter { it.isNotEmpty() }
                    ?: emptyList()
            }
            .first()
    }

    suspend fun addWatchedMovie(movieId: Int) {
        context.dataStore.edit { preferences ->
            val currentWatched = preferences[WATCHED_MOVIES]?.split(",")?.toMutableList() ?: mutableListOf()
            if (!currentWatched.contains(movieId.toString())) {
                currentWatched.add(movieId.toString())
                preferences[WATCHED_MOVIES] = currentWatched.joinToString(",")
            }
        }
    }

    suspend fun getWatchedMovies(): List<Int> {
        return context.dataStore.data
            .map { preferences ->
                preferences[WATCHED_MOVIES]?.split(",")?.filter { it.isNotEmpty() }
                    ?.map { it.toInt() } ?: emptyList()
            }
            .first()
    }

    suspend fun rateMovie(movieId: Int, rating: Float) {
        context.dataStore.edit { preferences ->
            val currentRatings = preferences[RATED_MOVIES]?.split(";")?.toMutableList() ?: mutableListOf()
            val ratingString = "$movieId:$rating"
            val existingIndex = currentRatings.indexOfFirst { it.startsWith("$movieId:") }
            if (existingIndex >= 0) {
                currentRatings[existingIndex] = ratingString
            } else {
                currentRatings.add(ratingString)
            }
            preferences[RATED_MOVIES] = currentRatings.joinToString(";")
        }
    }

    suspend fun getMovieRating(movieId: Int): Float? {
        return context.dataStore.data
            .map { preferences ->
                preferences[RATED_MOVIES]?.split(";")?.find { it.startsWith("$movieId:") }
                    ?.split(":")?.getOrNull(1)?.toFloatOrNull()
            }
            .first()
    }

    suspend fun getAllRatings(): Map<Int, Float> {
        return context.dataStore.data
            .map { preferences ->
                preferences[RATED_MOVIES]?.split(";")?.filter { it.isNotEmpty() }
                    ?.mapNotNull { rating ->
                        val parts = rating.split(":")
                        if (parts.size == 2) {
                            parts[0].toIntOrNull()?.to(parts[1].toFloatOrNull() ?: 0f)
                        } else null
                    }?.toMap() ?: emptyMap()
            }
            .first()
    }
} 