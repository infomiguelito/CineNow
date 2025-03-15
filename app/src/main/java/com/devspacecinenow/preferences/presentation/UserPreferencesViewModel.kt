package com.devspacecinenow.preferences.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devspacecinenow.preferences.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserPreferencesUiState(
    val availableGenres: List<String> = listOf(
        "Ação", "Aventura", "Animação", "Comédia", "Crime",
        "Documentário", "Drama", "Família", "Fantasia",
        "História", "Terror", "Música", "Mistério", "Romance",
        "Ficção Científica", "Cinema TV", "Thriller", "Guerra", "Faroeste"
    ),
    val selectedGenres: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class UserPreferencesViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserPreferencesUiState())
    val uiState: StateFlow<UserPreferencesUiState> = _uiState

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val savedGenres = repository.getSelectedGenres()
                _uiState.value = _uiState.value.copy(
                    selectedGenres = savedGenres.toSet(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar preferências: ${e.message}"
                )
            }
        }
    }

    fun addGenre(genre: String) {
        _uiState.value = _uiState.value.copy(
            selectedGenres = _uiState.value.selectedGenres + genre
        )
    }

    fun removeGenre(genre: String) {
        _uiState.value = _uiState.value.copy(
            selectedGenres = _uiState.value.selectedGenres - genre
        )
    }

    fun savePreferences(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.saveSelectedGenres(_uiState.value.selectedGenres.toList())
                onSaved()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao salvar preferências: ${e.message}"
                )
            }
        }
    }

    companion object {
        fun factory(repository: UserPreferencesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserPreferencesViewModel(repository) as T
                }
            }
        }
    }
} 