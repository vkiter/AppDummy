package edu.khokhlovvaleriy.appdummy.screens.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.khokhlovvaleriy.appdummy.screens.ItemLibro
import edu.khokhlovvaleriy.appdummy.screens.compartirLibro
import edu.khokhlovvaleriy.appdummy.screens.listado.LibrosUiState

// ─── screens/favoritos/PantallaFavoritos.kt ───
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(
    onNavegaADetalle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritosViewModel = viewModel(factory = FavoritosViewModel.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val estadoGrid = rememberLazyGridState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Mis favoritos") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val estado = uiState) {
                is LibrosUiState.Cargando ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                is LibrosUiState.Exito -> {
                    if (estado.libros.isEmpty()) {
                        // Estado vacío (R18)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Todavía no tienes favoritos",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Pulsa el corazón de un libro en el catálogo para añadirlo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            state = estadoGrid,
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(estado.libros, key = { it.id }) { libro ->
                                ItemLibro(
                                    libro = libro,
                                    onClick = { onNavegaADetalle(libro.id) },
                                    onToggleLeido = { viewModel.toggleLeido(libro) },
                                    onToggleFavorito = { viewModel.quitarFavorito(libro) },
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
                        Text(estado.mensaje, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = viewModel::cargarFavoritos) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}