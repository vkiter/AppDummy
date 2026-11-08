package edu.khokhlovvaleriy.appdummy.navegacion

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.khokhlovvaleriy.appdummy.screens.PantallaBienvenida
import edu.khokhlovvaleriy.appdummy.screens.PantallaGestionPermisos
import edu.khokhlovvaleriy.appdummy.screens.detalle.PantallaDetalle
import edu.khokhlovvaleriy.appdummy.screens.favoritos.PantallaFavoritos
import edu.khokhlovvaleriy.appdummy.screens.listado.PantallaListado

// ─── navegacion/AppNavigation.kt ───
// ÚNICO composable que conoce el NavController.
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // Destino actual, leído del back stack (se recompone al navegar)
    val entradaActual by navController.currentBackStackEntryAsState()
    val destinoActual = entradaActual?.destination

    // La barra inferior solo se muestra en Catálogo y Favoritos
    val mostrarBarra = itemsNavegacionPrincipal.any { item ->
        destinoActual?.hierarchy?.any { it.hasRoute(item.ruta::class) } == true
    }

    Scaffold(
        // Cada pantalla tiene su propio Scaffold y gestiona sus márgenes del sistema
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (mostrarBarra) {
                AppDummyBottomBar(
                    destinoActual = destinoActual,
                    onSeleccionar = { ruta ->
                        navController.navigate(ruta) {
                            // Vuelve al Listado (raíz de la pila) sin acumular pestañas
                            // y guarda el estado (scroll, ViewModel) de la pestaña que se deja
                            popUpTo<Listado> { saveState = true }
                            // No apila dos copias del mismo destino
                            launchSingleTop = true
                            // Recupera el estado guardado de la pestaña a la que se vuelve
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Inicio,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<Inicio> {
                PantallaBienvenida(
                    onEntrar = {
                        navController.navigate(Listado) {
                            // Elimina la bienvenida de la pila: Atrás en el Listado cierra la app
                            popUpTo<Inicio> { inclusive = true }
                        }
                    }
                )
            }

            composable<Listado> {
                PantallaListado(
                    onNavegaADetalle = { id -> navController.navigate(Detalle(id = id)) },
                    onAbrirPermisos = { navController.navigate(Permisos) }
                )
            }

            composable<Detalle> {
                // El id lo lee DetalleViewModel desde el SavedStateHandle
                PantallaDetalle(onVolver = { navController.navigateUp() })
            }

            composable<Favoritos> {
                PantallaFavoritos(
                    onNavegaADetalle = { id -> navController.navigate(Detalle(id = id)) }
                )
            }

            composable<Permisos> {
                PantallaGestionPermisos(onVolver = { navController.navigateUp() })
            }
        }
    }
}