package edu.khokhlovvaleriy.appdummy.screens.listado

import edu.khokhlovvaleriy.appdummy.data.model.Libro

sealed class LibrosUiState {
    // data object: genera un toString() legible ("Cargando")
    data object Cargando : LibrosUiState()
    data class Exito(val libros: List<Libro>) : LibrosUiState()
    data class Error(val mensaje: String) : LibrosUiState()
}