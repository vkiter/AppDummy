package edu.khokhlovvaleriy.appdummy.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Book
import androidx.compose.ui.graphics.vector.ImageVector

// ─── navegacion/ItemsNavegacion.kt ───
data class ItemNavegacion(
    val etiqueta: String,
    val ruta: Any,                 // ruta tipada (Listado, Favoritos...)
    val icono: ImageVector,
    val iconoSeleccionado: ImageVector = icono
)

// La NavigationBar se genera a partir de esta lista
val itemsNavegacionPrincipal = listOf(
    ItemNavegacion(
        etiqueta = "Catálogo",
        ruta = Listado,
        icono = Icons.Outlined.Book,
        iconoSeleccionado = Icons.Filled.Book
    ),
    ItemNavegacion(
        etiqueta = "Favoritos",
        ruta = Favoritos,
        icono = Icons.Filled.FavoriteBorder,
        iconoSeleccionado = Icons.Filled.Favorite
    )
)