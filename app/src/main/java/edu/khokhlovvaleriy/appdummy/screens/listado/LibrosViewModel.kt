package edu.khokhlovvaleriy.appdummy.screens.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.khokhlovvaleriy.appdummy.data.model.Libro
import edu.khokhlovvaleriy.appdummy.data.repository.LibrosRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class LibrosEvento {
    data class MostrarSnackbar(val mensaje: String) : LibrosEvento()
}


class LibrosViewModel(
    private val repository: LibrosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibrosUiState>(LibrosUiState.Cargando)
    val uiState: StateFlow<LibrosUiState> = _uiState.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _autores = MutableStateFlow(listOf("Todos"))
    val autores: StateFlow<List<String>> = _autores.asStateFlow()

    private val _autorSeleccionado = MutableStateFlow("Todos")
    val autorSeleccionado: StateFlow<String> = _autorSeleccionado.asStateFlow()


    private val _eventos = MutableSharedFlow<LibrosEvento>(replay = 0, extraBufferCapacity = 1)
    val eventos: SharedFlow<LibrosEvento> = _eventos.asSharedFlow()


    private var todosLosLibros: List<Libro> = emptyList()

    init {
        cargarLibros()
        observarCambios()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.value = LibrosUiState.Cargando
            try {
                todosLosLibros = repository.getLibros()
                _autores.value = listOf("Todos") + repository.getAutores()
                aplicarFiltros()
            } catch (e: Exception) {
                _uiState.value = LibrosUiState.Error(
                    e.message ?: "Error desconocido al cargar los libros"
                )
            }
        }
    }

    private fun observarCambios() {
        viewModelScope.launch {
            repository.libros.collect { libros ->
                if (_uiState.value is LibrosUiState.Exito) {
                    todosLosLibros = libros
                    aplicarFiltros()
                }
            }
        }
    }

    private fun aplicarFiltros() {
        val texto = _busqueda.value
        val autor = _autorSeleccionado.value
        val filtrados = todosLosLibros.filter { libro ->
            val coincideBusqueda = texto.isBlank() || libro.titulo.contains(texto, ignoreCase = true)
            val coincideAutor = autor == "Todos" || libro.autor == autor
            coincideBusqueda && coincideAutor
        }
        _uiState.value = LibrosUiState.Exito(filtrados)
    }

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
        if (_uiState.value is LibrosUiState.Exito) aplicarFiltros()
    }

    fun actualizarAutorSeleccionado(autor: String) {
        _autorSeleccionado.value = autor
        if (_uiState.value is LibrosUiState.Exito) aplicarFiltros()
    }

    fun toggleFavorito(libro: Libro) {
        repository.toggleFavorito(libro.id)
        viewModelScope.launch {
            _eventos.emit(
                LibrosEvento.MostrarSnackbar(
                    if (libro.esFavorito) "\"${libro.titulo}\" eliminado de favoritos"
                    else "\"${libro.titulo}\" añadido a favoritos"
                )
            )
        }
    }

    fun toggleLeido(libro: Libro) {
        repository.toggleLeido(libro.id)
        viewModelScope.launch {
            _eventos.emit(
                LibrosEvento.MostrarSnackbar(
                    if (libro.leido) "\"${libro.titulo}\" marcado como no leído"
                    else "\"${libro.titulo}\" marcado como leído"
                )
            )
        }
    }

    companion object {
        // Factory: el ViewModel no se crea dentro del composable
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LibrosViewModel(LibrosRepository.instancia)
            }
        }
    }
}