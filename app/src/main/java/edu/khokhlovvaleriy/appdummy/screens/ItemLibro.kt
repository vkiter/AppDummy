package edu.khokhlovvaleriy.appdummy.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import edu.khokhlovvaleriy.appdummy.R
import edu.khokhlovvaleriy.appdummy.model.LibroUI
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

@Composable
fun ItemLibro(
    libro: LibroUI,
    onToggleLeido: (Int) -> Unit,
    onToggleFavorito: (Int) -> Unit,
    onCompartir: (LibroUI) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
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
                    modifier = Modifier.width(200.dp)
                )
            else
                AsyncImage(
                    model = R.drawable.nocover,
                    contentDescription = "Portada de ${libro.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(200.dp)
                )

            Text(
                text = "${libro.autor} • ${libro.year}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onToggleLeido(libro.id) }
                ) {
                    Icon(
                        imageVector = if (libro.leido) Icons.Default.BookmarkAdded
                        else Icons.Default.BookmarkBorder,
                        contentDescription = if (libro.leido) "Quitar leído" else "Marcar como leído",
                        tint = if (libro.leido) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onToggleFavorito(libro.id) }
                ) {
                    Icon(
                        imageVector = if (libro.esFavorito) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = if (libro.esFavorito) "Quitar favorito" else "Añadir favorito",
                        tint = if (libro.esFavorito) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onCompartir(libro) }
                ) {
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


@Preview(showBackground = true)
@Composable
fun ItemLibroPreview() {
    AppDummyTheme {
        ItemLibro(
            libro = LibroUI(1, "Proyecto Hail Mary", "Andy Weir", 2021, esFavorito = true),
            onToggleLeido = { },
            onToggleFavorito = { },
            onCompartir = { }
        )
    }
}