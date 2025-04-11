package com.devspacecinenow.preferences.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user_preferences"
        )

        private val SELECTED_GENRES = stringPreferencesKey("selected_genres")
        private val WATCHED_MOVIES = stringPreferencesKey("watched_movies")
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

    suspend fun getWatchedMovies(): List<Int> {
        return context.dataStore.data
            .map { preferences ->
                preferences[WATCHED_MOVIES]?.split(",")?.filter { it.isNotEmpty() }
                    ?.map { it.toInt() } ?: emptyList()
            }
            .first()
    }
} 