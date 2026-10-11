package edu.khokhlovvaleriy.appdummy.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.khokhlovvaleriy.appdummy.model.librosEjemplo
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListado(modifier: Modifier = Modifier) {
    val context = LocalContext.current


    var busqueda by remember { mutableStateOf("") }
    var autorSeleccionado by remember { mutableStateOf("Todos") }
    var libros by remember { mutableStateOf(librosEjemplo) }

    val autores = listOf("Todos") + libros.map { it.autor }.distinct().sorted()


    val librosFiltrados = libros.filter { libro ->
        val coincideBusqueda = busqueda.isBlank() ||
                libro.titulo.contains(busqueda, ignoreCase = true)
        val coincideAutor = autorSeleccionado == "Todos" ||
                libro.autor == autorSeleccionado
        coincideBusqueda && coincideAutor
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("AppDummy") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar libros...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedVisibility(visible = busqueda.isNotEmpty()) {
                        IconButton(onClick = { busqueda = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(autores) { autor ->
                    FilterChip(
                        selected = autor == autorSeleccionado,
                        onClick = { autorSeleccionado = autor },
                        label = { Text(autor) }
                    )
                }
            }

            if (librosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
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
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(librosFiltrados, key = { it.id }) { libro ->
                        ItemLibro(
                            libro = libro,
                            onToggleLeido = { id ->
                                libros = libros.map {
                                    if (it.id == id) it.copy(leido = !it.leido) else it
                                }
                            },
                            onToggleFavorito = { id ->
                                libros = libros.map {
                                    if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
                                }
                            },
                            // ⚠️ R16: intent implícito ACTION_SEND (Tema 1D, apdo. 3.2)
                            onCompartir = { libroACompartir ->
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Te recomiendo '${libroACompartir.titulo}' de ${libroACompartir.autor}"
                                    )
                                }
                                context.startActivity(Intent.createChooser(intent, "Compartir con..."))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaListadoPreview() {
    AppDummyTheme {
        PantallaListado()
    }
}