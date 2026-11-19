package edu.bonetfrancisco.appdummy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.bonetfrancisco.appdummy.ui.theme.AppDummyTheme

sealed class EstadoPermiso {
    data object Concedido : EstadoPermiso()
    data object Pendiente : EstadoPermiso()
    data object Denegado : EstadoPermiso()
    data object DenegadoPermanentemente : EstadoPermiso()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionPermisos(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    // Comprobación inicial del estado del permiso de cámara
    var estadoPermiso by remember {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        mutableStateOf(if (concedido) EstadoPermiso.Concedido else EstadoPermiso.Pendiente)
    }

    // Lanzador para solicitar el permiso de cámara
    val solicitarPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        estadoPermiso = when {
            concedido -> EstadoPermiso.Concedido
            activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.CAMERA
            ) -> EstadoPermiso.DenegadoPermanentemente
            else -> EstadoPermiso.Denegado
        }
    }

    val abrirAjustes = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        estadoPermiso = if (concedido) {
            EstadoPermiso.Concedido
        } else {
            EstadoPermiso.DenegadoPermanentemente
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Permisos de AppDummy") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Desestructuramos un Triple con (icono, colorIcono, descripcion) según el estado
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
                    Icons.Default.Block,
                    MaterialTheme.colorScheme.error,
                    "Permiso de cámara denegado"
                )
                EstadoPermiso.DenegadoPermanentemente -> Triple(
                    Icons.Default.Block,
                    MaterialTheme.colorScheme.error,
                    "Activa el permiso de cámara en los ajustes de AppDummy"
                )
            }

            Icon(icono, contentDescription = null, modifier = Modifier.size(72.dp), tint = colorIcono)
            Spacer(modifier = Modifier.height(16.dp))
            Text(descripcion, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // Botón de acción según el estado actual
            when (estadoPermiso) {
                EstadoPermiso.Concedido -> Text("✓ Puedes usar la cámara en AppDummy")
                EstadoPermiso.Pendiente -> {
                    Button(
                        onClick = { solicitarPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Conceder permiso de cámara") }
                }
                EstadoPermiso.Denegado -> {
                    OutlinedButton(
                        onClick = { solicitarPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Volver a solicitar permiso") }
                }
                EstadoPermiso.DenegadoPermanentemente -> {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            abrirAjustes.launch(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Abrir Ajustes del sistema") }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaGestionPermisosPreview() {
    AppDummyTheme {
        PantallaGestionPermisos()
    }
}
