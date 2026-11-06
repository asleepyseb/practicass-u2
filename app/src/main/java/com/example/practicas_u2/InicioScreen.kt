package com.example.practicas_u2

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 1. Pantalla Padre (Dueña del estado centralizado)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResenaScreen(modifier : Modifier = Modifier) {
    // Estados locales
    var comida by remember { mutableStateOf(0) }
    var servicio by remember { mutableStateOf(0) }
    var ambiente by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    // Estado con Set (inmutable, se reemplaza por completo)
    var etiquetas by remember { mutableStateOf(setOf<String>()) }
    var publicada by remember { mutableStateOf(false) }

    // Valores derivados (se calculan automáticamente en cada recomposición)
    val todasCalificadas = comida > 0 && servicio > 0 && ambiente > 0
    val general = if (todasCalificadas) (comida + servicio + ambiente) / 3.0 else 0.0
    val puedePublicar = todasCalificadas && comentario.length >= 20

    val opcionesEtiquetas = listOf("Familiar", "Romántico", "Económico", "Vegetariano", "Rápido")

    if (publicada) {
        ResumenResena(general = general, comentario = comentario, etiquetas = etiquetas)
    } else {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            Text("Escribe tu reseña", style = MaterialTheme.typography.headlineMedium)
            Text("Restaurante El Mezquite", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Califica tu experiencia", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(16.dp))

            // Reutilización del mismo componente 3 veces
            Text("Comida", style = MaterialTheme.typography.bodyMedium)
            RatingBar(rating = comida, onRatingChange = { comida = it })

            Text("Servicio", style = MaterialTheme.typography.bodyMedium)
            RatingBar(rating = servicio, onRatingChange = { servicio = it })

            Text("Ambiente", style = MaterialTheme.typography.bodyMedium)
            RatingBar(rating = ambiente, onRatingChange = { ambiente = it })

            if (todasCalificadas) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Calificación general")
                    Text(
                        text = "%.1f".format(general),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ComentarioField(texto = comentario, onChange = { comentario = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Chips de etiquetas con selección múltiple
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                opcionesEtiquetas.forEach { etiqueta ->
                    FilterChip(
                        selected = etiquetas.contains(etiqueta),
                        onClick = {
                            etiquetas = if (etiquetas.contains(etiqueta)) {
                                etiquetas - etiqueta // Remueve creando un nuevo Set
                            } else {
                                etiquetas + etiqueta // Agrega creando un nuevo Set
                            }
                        },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { publicada = true },
                enabled = puedePublicar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar")
            }
        }
    }
}

// --- Composables "Stateless" ---

@Composable
fun RatingBar(rating: Int, onRatingChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    val descripciones = listOf("Malo", "Regular", "Bueno", "Muy bueno", "Excelente")

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            for (i in 1..5) {
                IconButton(onClick = {
                    // Reto extra: si toca la misma estrella, se desmarca poniéndola en 0
                    if (rating == i) onRatingChange(0) else onRatingChange(i)
                }) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Estrella $i",
                        tint = if (i <= rating) Color(0xFFFFB300) else Color.Gray
                    )
                }
            }
        }
        if (rating > 0) {
            Text(
                text = descripciones[rating - 1],
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ComentarioField(texto: String, onChange: (String) -> Unit, maximo: Int = 200) {
    val isError = texto.isNotEmpty() && texto.length < 20
    val isLimitReached = texto.length >= 180

    OutlinedTextField(
        value = texto,
        onValueChange = { if (it.length <= maximo) onChange(it) }, // Límite estricto de caracteres
        label = { Text("Comentario") },
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        minLines = 3,
        supportingText = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                if (isError) {
                    Text("Mínimo 20 caracteres")
                } else {
                    Spacer(modifier = Modifier.width(1.dp)) // Espaciador para mantener el contador a la derecha
                }
                Text(
                    text = "${texto.length}/$maximo",
                    color = if (isLimitReached) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        }
    )
}

@Composable
fun ResumenResena(general: Double, comentario: String, etiquetas: Set<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("¡Reseña Publicada!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Calificación general: %.1f".format(general), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(comentario)
            Spacer(modifier = Modifier.height(16.dp))

            if (etiquetas.isNotEmpty()) {
                Text("Etiquetas seleccionadas:", style = MaterialTheme.typography.labelMedium)
                Text(etiquetas.joinToString(" • "), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    ResenaScreen()
}