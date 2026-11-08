package edu.khokhlovvaleriy.appdummy.navegacion

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

// ─── navegacion/AppDummyBottomBar.kt ───
// No recibe el NavController: recibe el destino actual y un callback.
@Composable
fun AppDummyBottomBar(
    destinoActual: NavDestination?,
    onSeleccionar: (Any) -> Unit
) {
    NavigationBar {
        itemsNavegacionPrincipal.forEach { item ->
            // hierarchy: recorre desde el destino actual hasta la raíz del grafo
            val seleccionado = destinoActual?.hierarchy?.any {
                it.hasRoute(item.ruta::class)
            } == true

            NavigationBarItem(
                selected = seleccionado,
                onClick = { onSeleccionar(item.ruta) },
                icon = {
                    Icon(
                        imageVector = if (seleccionado) item.iconoSeleccionado else item.icono,
                        contentDescription = item.etiqueta
                    )
                },
                label = { Text(item.etiqueta) }
            )
        }
    }
}