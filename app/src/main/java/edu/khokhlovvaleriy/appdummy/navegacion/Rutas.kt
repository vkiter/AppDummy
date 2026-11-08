package edu.khokhlovvaleriy.appdummy.navegacion

import kotlinx.serialization.Serializable

// ─── navegacion/Rutas.kt ───
// Rutas tipadas: sin argumentos → data object; con argumentos → data class
@Serializable
data object Inicio

@Serializable
data object Listado

@Serializable
data class Detalle(val id: Int)

@Serializable
data object Favoritos

@Serializable
data object Permisos