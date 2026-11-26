package com.example.practicas_u2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicas_u2.Model.InscripcionUiState
import com.example.practicas_u2.ViewModel.InscripcionViewModel

@Composable
fun InscripcionRoute(
    modifier: Modifier = Modifier,
    viewModel: InscripcionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InscripcionScreen(
        uiState = uiState,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onEdadChange = viewModel::onEdadChange,
        onTallerChange = viewModel::onTallerChange,
        onAceptaAvisoChange = viewModel::onAceptaAvisoChange,
        onEnviarClick = viewModel::onEnviarClick,
        onNuevaInscripcion = viewModel::onNuevaInscripcion,
        modifier = modifier
    )
}

@Composable
fun InscripcionScreen(
    uiState: InscripcionUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onEdadChange: (String) -> Unit,
    onTallerChange: (String) -> Unit,
    onAceptaAvisoChange: (Boolean) -> Unit,
    onEnviarClick: () -> Unit,
    onNuevaInscripcion: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.inscrito) {
        ConfirmacionScreen(
            nombre = uiState.nombre.trim(),
            correo = uiState.correo.trim(),
            edad = uiState.edadTexto,
            taller = uiState.tallerSeleccionado,
            onNueva = onNuevaInscripcion,
            modifier = modifier
        )
    } else {
        InscripcionForm(
            uiState = uiState,
            onNombreChange = onNombreChange,
            onCorreoChange = onCorreoChange,
            onEdadChange = onEdadChange,
            onTallerChange = onTallerChange,
            onAceptaAvisoChange = onAceptaAvisoChange,
            onEnviarClick = onEnviarClick,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InscripcionForm(
    uiState: InscripcionUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onEdadChange: (String) -> Unit,
    onTallerChange: (String) -> Unit,
    onAceptaAvisoChange: (Boolean) -> Unit,
    onEnviarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpandido by remember { mutableStateOf(false) }
    val lugaresRestantes = uiState.tallerActual?.lugaresDisponibles ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Inscripción a talleres",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Casa del Lago · cupo limitado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoTexto(
            label = "Nombre completo",
            value = uiState.nombre,
            onValueChange = onNombreChange,
            error = uiState.errorNombre,
            keyboardType = KeyboardType.Text
        )

        CampoTexto(
            label = "Correo",
            value = uiState.correo,
            onValueChange = onCorreoChange,
            error = uiState.errorCorreo,
            keyboardType = KeyboardType.Email
        )

        CampoTexto(
            label = "Edad",
            value = uiState.edadTexto,
            onValueChange = onEdadChange,
            error = uiState.errorEdad,
            keyboardType = KeyboardType.Number
        )

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.tallerSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Taller") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                uiState.talleres.forEach { taller ->
                    DropdownMenuItem(
                        text = {
                            Text("${taller.nombre} (${taller.lugaresDisponibles} lugares)")
                        },
                        onClick = {
                            onTallerChange(taller.nombre)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.aceptaAviso,
                onCheckedChange = onAceptaAvisoChange
            )
            Text(
                text = "Acepto el aviso de privacidad",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiState.errorAviso != null) {
            Text(
                text = uiState.errorAviso.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onEnviarClick,
            enabled = lugaresRestantes > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Inscribirme")
        }

        Text(
            text = "Quedan $lugaresRestantes lugares en este taller",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ConfirmacionScreen(
    nombre: String,
    correo: String,
    edad: String,
    taller: String,
    onNueva: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Inscripción confirmada",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tu lugar en Casa del Lago ha sido registrado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nombre: $nombre")
                Text("Correo: $correo")
                Text("Edad: $edad años")
                Text("Taller: $taller")
            }
        }

        OutlinedButton(
            onClick = onNueva,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva inscripción")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InscripcionScreenPreview() {
    Surface {
        InscripcionScreen(
            uiState = InscripcionUiState(
                nombre = "Ana López",
                correo = "ana.lopez@",
                edadTexto = "25",
                tallerSeleccionado = "Fotografía",
                aceptaAviso = true,
                intentoEnviar = true
            ),
            onNombreChange = {},
            onCorreoChange = {},
            onEdadChange = {},
            onTallerChange = {},
            onAceptaAvisoChange = {},
            onEnviarClick = {},
            onNuevaInscripcion = {}
        )
    }
}