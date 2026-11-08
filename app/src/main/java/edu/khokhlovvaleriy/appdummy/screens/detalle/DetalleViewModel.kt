package edu.khokhlovvaleriy.appdummy.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import edu.khokhlovvaleriy.appdummy.data.repository.LibrosRepository
import edu.khokhlovvaleriy.appdummy.navegacion.Detalle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─── screens/detalle/DetalleViewModel.kt ───
class DetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LibrosRepository
) : ViewModel() {

    // Navigation guarda los argumentos de la ruta Detalle(id) en el SavedStateHandle
    private val libroId: Int = savedStateHandle.toRoute<Detalle>().id

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    private fun cargarDetalle() {
        viewModelScope.launch {
            val libro = repository.getLibroPorId(libroId)
            _uiState.value = if (libro != null) DetalleUiState.Exito(libro)
            else DetalleUiState.NoEncontrado
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetalleViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = LibrosRepository.instancia
                )
            }
        }
    }
}