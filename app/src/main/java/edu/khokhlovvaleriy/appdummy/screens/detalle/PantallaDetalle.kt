package edu.khokhlovvaleriy.appdummy.screens.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import edu.khokhlovvaleriy.appdummy.R
import edu.khokhlovvaleriy.appdummy.data.model.Libro

// ─── screens/detalle/PantallaDetalle.kt ───
// El id del libro no llega como parámetro: el ViewModel lo lee del SavedStateHandle.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalleViewModel = viewModel(factory = DetalleViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val titulo = (uiState as? DetalleUiState.Exito)?.libro?.titulo ?: "Detalle"
                    Text(titulo)
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val estado = uiState) {
                is DetalleUiState.Cargando ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                is DetalleUiState.Exito -> ContenidoDetalle(libro = estado.libro)

                is DetalleUiState.NoEncontrado -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Libro no encontrado", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onVolver) { Text("Volver al catálogo") }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContenidoDetalle(libro: Libro) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = if (libro.cover.isNotBlank()) libro.cover else R.drawable.nocover,
            contentDescription = "Portada de ${libro.titulo}",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(240.dp)
        )
        Text(libro.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(
            "${libro.autor} • ${libro.year}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text("ISBN: ${libro.isbn}", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = { },
                label = { Text(if (libro.esFavorito) "Favorito" else "No favorito") },
                leadingIcon = {
                    Icon(
                        if (libro.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
            )
            AssistChip(
                onClick = { },
                label = { Text(if (libro.leido) "Leído" else "Pendiente") },
                leadingIcon = {
                    Icon(
                        if (libro.leido) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                        contentDescription = null
                    )
                }
            )
        }
    }
}