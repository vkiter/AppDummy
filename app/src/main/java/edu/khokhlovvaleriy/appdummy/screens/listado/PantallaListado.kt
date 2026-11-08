package edu.khokhlovvaleriy.appdummy.screens.listado

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.khokhlovvaleriy.appdummy.screens.ItemLibro
import edu.khokhlovvaleriy.appdummy.screens.compartirLibro
import kotlinx.coroutines.launch

// ─── screens/listado/PantallaListado.kt ───
// No recibe el NavController: solo callbacks de navegación.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListado(
    onNavegaADetalle: (Int) -> Unit,
    onAbrirPermisos: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibrosViewModel = viewModel(factory = LibrosViewModel.Factory)
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    // Posición del scroll de la rejilla (se conserva al cambiar de pestaña)
    val estadoGrid = rememberLazyGridState()

    // collectAsStateWithLifecycle: activo en primer plano, pausado en segundo plano
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val autores by viewModel.autores.collectAsStateWithLifecycle()
    val autorSeleccionado by viewModel.autorSeleccionado.collectAsStateWithLifecycle()

    // Eventos de un solo disparo (Snackbar). LaunchedEffect(Unit): se lanza al montar.
    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is LibrosEvento.MostrarSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    launch { snackbarHostState.showSnackbar(evento.mensaje) }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("AppDummy") },
                actions = {
                    // R19: acceso a la pantalla de permisos
                    IconButton(onClick = onAbrirPermisos) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Permisos de cámara")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Barra de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = viewModel::actualizarBusqueda,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar libros...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedVisibility(visible = busqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.actualizarBusqueda("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true
            )

            // Chips de autores
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(autores) { autor ->
                    FilterChip(
                        selected = autor == autorSeleccionado,
                        onClick = { viewModel.actualizarAutorSeleccionado(autor) },
                        label = { Text(autor) }
                    )
                }
            }

            // when exhaustivo sobre la sealed class
            when (val estado = uiState) {
                is LibrosUiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LibrosUiState.Exito -> {
                    if (estado.libros.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.SearchOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Sin resultados para \"$busqueda\"",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            state = estadoGrid,
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(estado.libros, key = { it.id }) { libro ->
                                ItemLibro(
                                    libro = libro,
                                    onClick = { onNavegaADetalle(libro.id) },
                                    onToggleLeido = { viewModel.toggleLeido(libro) },
                                    onToggleFavorito = { viewModel.toggleFavorito(libro) },
                                    onCompartir = { compartirLibro(context, libro) }
                                )
                            }
                        }
                    }
                }

                is LibrosUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = estado.mensaje, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = viewModel::cargarLibros) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}