package edu.khokhlovvaleriy.appdummy.screens.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.khokhlovvaleriy.appdummy.data.model.Libro
import edu.khokhlovvaleriy.appdummy.data.repository.LibrosRepository
import edu.khokhlovvaleriy.appdummy.screens.listado.LibrosUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─── screens/favoritos/FavoritosViewModel.kt ───
// Reutiliza LibrosUiState (Cargando / Exito / Error) con la lista de favoritos.
class FavoritosViewModel(
    private val repository: LibrosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibrosUiState>(LibrosUiState.Cargando)
    val uiState: StateFlow<LibrosUiState> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.value = LibrosUiState.Cargando
            try {
                repository.getLibros()   // primera carga (latencia simulada)
            } catch (e: Exception) {
                _uiState.value = LibrosUiState.Error(
                    e.message ?: "Error desconocido al cargar los favoritos"
                )
                return@launch
            }
            // A partir de aquí, cualquier cambio en el repositorio actualiza la lista
            repository.libros.collect { libros ->
                _uiState.value = LibrosUiState.Exito(libros.filter { it.esFavorito })
            }
        }
    }

    fun quitarFavorito(libro: Libro) = repository.toggleFavorito(libro.id)

    fun toggleLeido(libro: Libro) = repository.toggleLeido(libro.id)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FavoritosViewModel(LibrosRepository.instancia)
            }
        }
    }
}