package edu.khokhlovvaleriy.appdummy.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionPermisos(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current


    var estadoPermiso by remember {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        mutableStateOf<EstadoPermiso>(
            if (concedido) EstadoPermiso.Concedido else EstadoPermiso.Pendiente
        )
    }


    val solicitarPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        estadoPermiso = when {
            concedido -> EstadoPermiso.Concedido
            estadoPermiso == EstadoPermiso.Denegado -> EstadoPermiso.DenegadoPermanentemente
            else -> EstadoPermiso.Denegado
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Permisos de AppDummy") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val (icono, colorIcono, descripcion) = when (estadoPermiso) {
                EstadoPermiso.Concedido -> Triple(
                    Icons.Default.CheckCircle,
                    MaterialTheme.colorScheme.primary,
                    "Cámara disponible"
                )
                EstadoPermiso.Pendiente -> Triple(
                    Icons.Default.CameraAlt,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    "La app necesita acceso a la cámara para capturar imágenes de portadas"
                )
                EstadoPermiso.Denegado -> Triple(
                    Icons.Default.Warning,
                    MaterialTheme.colorScheme.tertiary,
                    "Has denegado el permiso. Sin él no podrás fotografiar portadas"
                )
                EstadoPermiso.DenegadoPermanentemente -> Triple(
                    Icons.Default.Block,
                    MaterialTheme.colorScheme.error,
                    "Permiso bloqueado. Actívalo manualmente desde los Ajustes del sistema"
                )
            }

            Icon(icono, contentDescription = null, modifier = Modifier.size(72.dp), tint = colorIcono)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                descripcion,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            when (estadoPermiso) {
                EstadoPermiso.Concedido ->
                    Text("✓ Puedes usar la cámara en AppDummy")

                EstadoPermiso.Pendiente ->
                    Button(
                        onClick = { solicitarPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Conceder permiso de cámara") }

                EstadoPermiso.Denegado ->
                    Button(
                        onClick = { solicitarPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Volver a intentar") }

                EstadoPermiso.DenegadoPermanentemente ->
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Abrir Ajustes del sistema") }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaGestionPermisosPreview() {
    AppDummyTheme {
        PantallaGestionPermisos(onVolver = { })
    }
}