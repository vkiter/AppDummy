package edu.khokhlovvaleriy.appdummy.screens.detalle

import edu.khokhlovvaleriy.appdummy.data.model.Libro

// ─── screens/detalle/DetalleUiState.kt ───
sealed class DetalleUiState {
    data object Cargando : DetalleUiState()
    data class Exito(val libro: Libro) : DetalleUiState()
    data object NoEncontrado : DetalleUiState()
}