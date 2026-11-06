package com.example.practicas_u2

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. Estructura de datos estricta
data class Pregunta(
    val texto: String,
    val opciones: List<String>,
    val indiceCorrecto: Int
)

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    // 2. Estado Centralizado (Hoisted State)
    val bancoPreguntas = remember {
        listOf(
            Pregunta("¿Qué modificador se usa para que un elemento ocupe todo el ancho?", listOf("fillMaxHeight()", "fillMaxSize()", "fillMaxWidth()"), 2),
            Pregunta("¿Cuál es el contenedor ideal para colocar vistas una al lado de la otra?", listOf("Row", "Column", "Box"), 0),
            Pregunta("¿Qué función de Compose retiene los valores ante las recomposiciones?", listOf("useState", "remember", "keepState"), 1)
        )
    }

    var indicePreguntaActual by remember { mutableIntStateOf(0) }
    var puntuacion by remember { mutableIntStateOf(0) }
    var quizTerminado by remember { mutableStateOf(false) }

    // 3. Distribuidor de la Interfaz
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (quizTerminado) {
            PantallaResultados(
                puntuacion = puntuacion,
                total = bancoPreguntas.size,
                onReiniciar = {
                    // El padre resetea la memoria
                    indicePreguntaActual = 0
                    puntuacion = 0
                    quizTerminado = false
                }
            )
        } else {
            PantallaPregunta(
                pregunta = bancoPreguntas[indicePreguntaActual],
                numeroActual = indicePreguntaActual + 1,
                totalPreguntas = bancoPreguntas.size,
                onOpcionSeleccionada = { indiceSeleccionado ->
                    // El padre evalúa la respuesta y avanza
                    if (indiceSeleccionado == bancoPreguntas[indicePreguntaActual].indiceCorrecto) {
                        puntuacion++
                    }

                    if (indicePreguntaActual < bancoPreguntas.size - 1) {
                        indicePreguntaActual++
                    } else {
                        quizTerminado = true
                    }
                }
            )
        }
    }
}

// 4. Componente Hijo Visual (Stateless) - Pantalla de preguntas
@Composable
fun PantallaPregunta(
    pregunta: Pregunta,
    numeroActual: Int,
    totalPreguntas: Int,
    onOpcionSeleccionada: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pregunta $numeroActual de $totalPreguntas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = pregunta.texto,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))

        pregunta.opciones.forEachIndexed { index, opcion ->
            Button(
                onClick = { onOpcionSeleccionada(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = opcion, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

// 5. Componente Hijo Visual (Stateless) - Resultados
@Composable
fun PantallaResultados(
    puntuacion: Int,
    total: Int,
    onReiniciar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¡Quiz Completado!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu puntuación: $puntuacion / $total",
            style = MaterialTheme.typography.displayMedium,
            color = if (puntuacion == total) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = onReiniciar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a intentar", modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    MaterialTheme {
        QuizScreen()
    }
}
@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    QuizScreen()
}