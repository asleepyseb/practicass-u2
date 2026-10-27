package com.example.practicas_u2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Enum Class con propiedades
enum class Interlineado(val factor: Float) {
    COMPACTO(1.0f),
    NORMAL(1.3f),
    AMPLIO(1.6f)
}

// 2. Pantalla Padre (Dueña del estado)
@Composable
fun LecturaScreen(modifier: Modifier = Modifier) {
    // El estado se eleva (State Hoisting) a este nivel
    var tamano by remember { mutableFloatStateOf(18f) }
    var interlineado by remember { mutableStateOf(Interlineado.NORMAL) }
    var modoOscuro by remember { mutableStateOf(false) }
    var usaSerif by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Ajustes de lectura", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Controles (recibe valores y eventos)
        ControlesLectura(
            tamano = tamano,
            onTamanoChange = { tamano = it },
            interlineado = interlineado,
            onInterlineadoChange = { interlineado = it },
            modoOscuro = modoOscuro,
            onModoOscuroChange = { modoOscuro = it },
            usaSerif = usaSerif,
            onUsaSerifChange = { usaSerif = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Vista Previa (Solo lee los valores)
        VistaPrevia(
            texto = "Era una noche tranquila en el pueblo cuando las campanas comenzaron a sonar, y nadie supo por qué.",
            tamano = tamano,
            interlineado = interlineado,
            modoOscuro = modoOscuro,
            usaSerif = usaSerif
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Botón Restablecer
        OutlinedButton(
            onClick = {
                tamano = 18f
                interlineado = Interlineado.NORMAL
                modoOscuro = false
                usaSerif = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restablecer")
        }
    }
}

// --- Composables "Stateless" (Sin estado propio) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlesLectura(
    tamano: Float, onTamanoChange: (Float) -> Unit,
    interlineado: Interlineado, onInterlineadoChange: (Interlineado) -> Unit,
    modoOscuro: Boolean, onModoOscuroChange: (Boolean) -> Unit,
    usaSerif: Boolean, onUsaSerifChange: (Boolean) -> Unit
) {
    Column {
        // Slider de tamaño
        Text("Tamaño de letra: ${tamano.toInt()} sp", color = MaterialTheme.colorScheme.primary)
        Slider(
            value = tamano,
            onValueChange = onTamanoChange, // El evento sube al padre
            valueRange = 14f..32f
        )

        // RadioButtons de interlineado
        Text("Interlineado")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Interlineado.entries.forEach { opcion ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (interlineado == opcion),
                        onClick = { onInterlineadoChange(opcion) }
                    )
                    Text(opcion.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        // Switch de modo oscuro
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modo oscuro")
            Switch(checked = modoOscuro, onCheckedChange = onModoOscuroChange)
        }

        // Chips de fuente
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Fuente: ")
            FilterChip(
                selected = usaSerif,
                onClick = { onUsaSerifChange(true) },
                label = { Text("Serif") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = !usaSerif,
                onClick = { onUsaSerifChange(false) },
                label = { Text("Sans") }
            )
        }
    }
}

@Composable
fun VistaPrevia(
    texto: String, tamano: Float,
    interlineado: Interlineado, modoOscuro: Boolean, usaSerif: Boolean
) {
    // Calculamos colores y fuentes derivados de los estados
    val bgColor = if (modoOscuro) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (modoOscuro) Color.White else Color.Black
    val font = if (usaSerif) FontFamily.Serif else FontFamily.SansSerif

    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = texto,
            fontSize = tamano.sp,
            lineHeight = (tamano * interlineado.factor).sp,
            color = textColor,
            fontFamily = font,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    LecturaScreen()
}