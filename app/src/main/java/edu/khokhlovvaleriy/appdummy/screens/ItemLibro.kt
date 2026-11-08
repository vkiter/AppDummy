package edu.khokhlovvaleriy.appdummy.screens

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import edu.khokhlovvaleriy.appdummy.R
import edu.khokhlovvaleriy.appdummy.data.model.Libro
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

// ─── screens/ItemLibro.kt ───
// Tarjeta de un libro. La usan el Listado y Favoritos.
@Composable
fun ItemLibro(
    libro: Libro,
    onClick: () -> Unit,
    onToggleLeido: () -> Unit,
    onToggleFavorito: () -> Unit,
    onCompartir: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(libro.titulo, style = MaterialTheme.typography.titleSmall)

            if (Patterns.WEB_URL.matcher(libro.cover).matches())
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(libro.cover)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Portada de ${libro.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                )
            else // Si no es una URL válida, se muestra una imagen por defecto
                AsyncImage(
                    model = R.drawable.nocover,
                    contentDescription = "Portada de ${libro.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                )

            Text(
                text = "${libro.autor} • ${libro.year}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(modifier = Modifier.weight(1f), onClick = onToggleLeido) {
                    Icon(
                        imageVector = if (libro.leido) Icons.Default.BookmarkAdded
                        else Icons.Default.BookmarkBorder,
                        contentDescription = if (libro.leido) "Quitar leído" else "Marcar como leído",
                        tint = if (libro.leido) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(modifier = Modifier.weight(1f), onClick = onToggleFavorito) {
                    Icon(
                        imageVector = if (libro.esFavorito) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = if (libro.esFavorito) "Quitar favorito" else "Añadir favorito",
                        tint = if (libro.esFavorito) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(modifier = Modifier.weight(1f), onClick = onCompartir) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Intent implícito ACTION_SEND (Tema 1D). Lo usan el Listado y Favoritos.
fun compartirLibro(context: Context, libro: Libro) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Te recomiendo '${libro.titulo}' de ${libro.autor}")
    }
    context.startActivity(Intent.createChooser(intent, "Compartir con..."))
}

@Preview(showBackground = true)
@Composable
fun ItemLibroPreview() {
    AppDummyTheme {
        ItemLibro(
            libro = Libro(1, "Proyecto Hail Mary", "Andy Weir", 2021, esFavorito = true),
            onClick = { },
            onToggleLeido = { },
            onToggleFavorito = { },
            onCompartir = { }
        )
    }
}