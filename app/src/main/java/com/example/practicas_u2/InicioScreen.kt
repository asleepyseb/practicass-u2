package com.example.practicas_u2

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

// 1. Modelos y Enums
data class Tarea(val id: Int, val titulo: String, val prioridad: Prioridad, val hecha: Boolean = false)

enum class Prioridad(val color: androidx.compose.ui.graphics.Color) {
    ALTA(androidx.compose.ui.graphics.Color(0xFFE57373)),
    MEDIA(androidx.compose.ui.graphics.Color(0xFFFFD54F)),
    BAJA(androidx.compose.ui.graphics.Color(0xFF81C784))
}

enum class Filtro { TODAS, PENDIENTES, HECHAS }

// 2. Pantalla Principal (Controladora de Estado)
@Composable
fun TareasScreen(modifier : Modifier = Modifier) {
    // Estado de la lista
    val tareas = remember { mutableStateListOf<Tarea>() }
    var idCounter by remember { mutableStateOf(0) }

    // Estado del formulario de nueva tarea
    var textoNueva by remember { mutableStateOf("") }
    var prioridadNueva by remember { mutableStateOf(Prioridad.MEDIA) }

    // Estado de filtros
    var filtroActual by remember { mutableStateOf(Filtro.TODAS) }

    // Estado del diálogo
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Valores derivados
    val tareasVisibles = when (filtroActual) {
        Filtro.TODAS -> tareas
        Filtro.PENDIENTES -> tareas.filter { !it.hecha }
        Filtro.HECHAS -> tareas.filter { it.hecha }
    }
    // Reto extra: Ordenar visibles por prioridad (Alta primero) sin alterar la lista original
    val tareasOrdenadas = tareasVisibles.sortedBy { it.prioridad.ordinal }
    val pendientes = tareas.count { !it.hecha }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("Mis pendientes", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Formulario de entrada
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textoNueva,
                onValueChange = { textoNueva = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (textoNueva.isNotBlank()) {
                        tareas.add(Tarea(idCounter++, textoNueva, prioridadNueva))
                        textoNueva = "" // Limpiar el campo
                    }
                },
                enabled = textoNueva.isNotBlank(), // Validación
                modifier = Modifier.size(56.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }

        // Selector de Prioridad para la nueva tarea
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            Text("Prioridad")
            Spacer(modifier = Modifier.width(8.dp))
            Prioridad.entries.forEach { prio ->
                FilterChip(
                    selected = prioridadNueva == prio,
                    onClick = { prioridadNueva = prio },
                    label = { Text(prio.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Filtros de vista
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Filtro.entries.forEach { f ->
                FilterChip(
                    selected = filtroActual == f,
                    onClick = { filtroActual = f },
                    label = { Text(f.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de tareas
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(tareasOrdenadas, key = { _, tarea -> tarea.id }) { _, tarea ->
                TareaItem(
                    tarea = tarea,
                    onCheckedChange = { nuevoEstado ->
                        // Actualizar la tarea inmutable en la lista original
                        val index = tareas.indexOfFirst { it.id == tarea.id }
                        if (index != -1) {
                            tareas[index] = tareas[index].copy(hecha = nuevoEstado)
                        }
                    },
                    onDelete = {
                        tareas.removeIf { it.id == tarea.id }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resumen
        Text("${pendientes} pendientes de ${tareas.size}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para borrar completadas
        OutlinedButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = tareas.any { it.hecha }
        ) {
            Text("Borrar completadas")
        }
    }

    // Diálogo de confirmación
    if (mostrarDialogo) {
        val completadas = tareas.count { it.hecha }
        ConfirmarBorradoDialog(
            cantidad = completadas,
            onConfirm = {
                tareas.removeIf { it.hecha }
                mostrarDialogo = false
            },
            onDismiss = { mostrarDialogo = false }
        )
    }
}

// 3. Componente TareaItem (Stateless)
@Composable
fun TareaItem(tarea: Tarea, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.hecha,
            onCheckedChange = onCheckedChange
        )
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(
                text = tarea.titulo,
                textDecoration = if (tarea.hecha) TextDecoration.LineThrough else TextDecoration.None
            )
            Surface(
                color = tarea.prioridad.color.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = tarea.prioridad.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = tarea.prioridad.color,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, contentDescription = "Eliminar tarea")
        }
    }
}

// 4. Componente Diálogo (Stateless)
@Composable
fun ConfirmarBorradoDialog(cantidad: Int, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Borrar tareas completadas?") },
        text = { Text("Se eliminarán $cantidad tareas completadas. Esta acción no se puede deshacer.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Borrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    TareasScreen()
}