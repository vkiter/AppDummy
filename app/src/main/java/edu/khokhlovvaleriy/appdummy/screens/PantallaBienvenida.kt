package edu.khokhlovvaleriy.appdummy.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBienvenida(
    onEntrar: () -> Unit,
    modifier: Modifier = Modifier          // Modifier como parámetro opcional
) {
    // rememberSaveable en vez de remember → el nombre sobrevive a la rotación
    var nombreUsuario by rememberSaveable { mutableStateOf("") }
    val botonHabilitado = nombreUsuario.trim().length >= 3   // estado derivado

    // Eestructura con Scaffold y TopAppBar
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("AppDummy") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)    // SIEMPRE aplicar el padding del Scaffold
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono y título (R1)
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AppDummy",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Tu catálogo de libros",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Campo de nombre con texto de apoyo
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("¿Cómo te llamas?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("Mínimo 3 caracteres (${nombreUsuario.length}/3)")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón habilitado solo con 3+ caracteres
            Button(
                onClick = onEntrar,
                enabled = botonHabilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (botonHabilitado) "Entrar como ${nombreUsuario.trim()}" else "Entrar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaBienvenidaPreview() {
    AppDummyTheme {                       // el tema del proyecto
        PantallaBienvenida(onEntrar = { })
    }
}