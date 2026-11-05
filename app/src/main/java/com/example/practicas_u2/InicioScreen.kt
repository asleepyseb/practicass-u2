package com.example.practicas_u2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat

fun validarNombre(nombre: String): String? =
    if (nombre.length >= 3 ) null else "El nombre debe tener al menos 3 caracteres"

fun validarCorreo(correo:String): String? =
    if(correo.contains("@") && correo.contains(".")) null else "Ingrese un correo valido"

fun validarEdad(edadTexto: String): String ? {
    val edad = edadTexto.toIntOrNull()
    return if (edad != null && edad in 12..99) null else "La edad debe ser entre 12 y 99"
}
@Composable
fun InscripcionScreen(modifier: Modifier = Modifier) {
    // Estados principales
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var taller by remember { mutableStateOf("Fotografía") }
    var aceptaAviso by remember { mutableStateOf(false) }

    var intentoEnviar by remember { mutableStateOf(false) }
    var inscrito by remember { mutableStateOf(false) } // define qué pantalla mostrar

    Box(modifier = modifier) {
        // Lógica de "Navegación" basada en estado
        if (inscrito) {
            ConfirmacionScreen(
                nombre = nombre,
                correo = correo,
                taller = taller,
                onNueva = {
                    // Restablecer el estado a los valores por defecto
                    nombre = ""
                    correo = ""
                    edad = ""
                    taller = "Fotografía"
                    aceptaAviso = false
                    intentoEnviar = false
                    inscrito = false
                }
            )
        } else {
            InscripcionForm(
                nombre = nombre, onNombreChange = { nombre = it },
                correo = correo, onCorreoChange = { correo = it },
                edad = edad, onEdadChange = { edad = it },
                taller = taller, onTallerChange = { taller = it },
                aceptaAviso = aceptaAviso, onAvisoChange = { aceptaAviso = it },
                intentoEnviar = intentoEnviar,
                onInscribir = {
                    intentoEnviar = true
                    // Solo inscribe si todas las validaciones retornan null (sin error) y acepta el aviso
                    if (validarNombre(nombre) == null &&
                        validarCorreo(correo) == null &&
                        validarEdad(edad) == null &&
                        aceptaAviso
                    ) {
                        inscrito = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscripcionForm(
    nombre: String, onNombreChange: (String) -> Unit,
    correo: String, onCorreoChange: (String) -> Unit,
    edad: String, onEdadChange: (String) -> Unit,
    taller: String, onTallerChange: (String) -> Unit,
    aceptaAviso: Boolean, onAvisoChange: (Boolean) -> Unit,
    intentoEnviar: Boolean,
    onInscribir: ()-> Unit
    ){
    val errorNombre = if(intentoEnviar) validarNombre(nombre) else null
    val errorCorreo = if(intentoEnviar) validarCorreo(correo) else null
    val errorEdad = if(intentoEnviar) validarEdad(edad) else null

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()){
        Text("Inscripcion a talleres")
        Text("Casa del lago - Cupo limitado")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre completo")},
            isError = errorNombre != null,
            supportingText = {errorNombre?.let {Text(it)}},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo")},
            isError = errorCorreo != null,
            supportingText = {errorCorreo?.let { Text(it) }},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = edad,
            onValueChange = onEdadChange,
            label = { Text("Edad")},
            isError = errorEdad != null,
            supportingText = {errorEdad?.let { Text(it) }},
            modifier = Modifier.fillMaxWidth()
        )

        var expandido by remember {mutableStateOf(false)}
        val opciones = listOf("Pintura", "Fotografia","Guitarra" )

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = {expandido = !expandido}
        ) {
            OutlinedTextField(
                value = taller,
                onValueChange = {},
                readOnly = true,
                label = {Text("Taller")},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)},
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = {expandido = false}
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = {Text(opcion) },
                        onClick = {
                            onTallerChange(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = aceptaAviso, onCheckedChange = onAvisoChange)
            Text("Acepto el aviso de privacidad")
        }
        if (intentoEnviar && !aceptaAviso) {
            Text("Debes aceptar el aviso para continuar", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onInscribir, modifier = Modifier.fillMaxWidth()) {
            Text("Inscribirme")
        }
    }
}

@Composable
fun ConfirmacionScreen(nombre: String, correo: String, taller: String, onNueva: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Inscripción exitosa!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre: $nombre")
        Text("Correo: $correo")
        Text("Taller: $taller")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNueva) {
            Text("Nueva inscripción")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    InscripcionScreen()
}