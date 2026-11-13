package com.example.practicas_u2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicas_u2.ui.theme.Practicasu2Theme

data class ElementoLista(
    val nombre: String,
    val colorString: String
)

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var listaElementos by rememberSaveable { mutableStateOf(listOf<ElementoLista>()) }
    var nombreEscrito by rememberSaveable { mutableStateOf("") }
    var colorEscrito by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = nombreEscrito,
                    onValueChange = { nombreEscrito = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = colorEscrito,
                    onValueChange = { colorEscrito = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Color (Ej: Rojo, Azul, etc.)") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (nombreEscrito.isNotBlank()) {
                            listaElementos = listaElementos + ElementoLista(nombreEscrito, colorEscrito)
                            nombreEscrito = ""
                            colorEscrito = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar")
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            items(listaElementos) { elemento ->
                MiCard(elemento = elemento)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun parseColor(colorString: String): Color {
    return when (colorString.trim().lowercase()) {
        "rojo", "red" -> Color.Red
        "azul", "blue" -> Color.Blue
        "verde", "green" -> Color.Green
        "amarillo", "yellow" -> Color.Yellow
        "cyan" -> Color.Cyan
        "magenta" -> Color.Magenta
        "gris", "gray" -> Color.Gray
        "negro", "black" -> Color.Black
        else -> Color.White
        }
    }
}

@Composable
fun MiCard(
    modifier: Modifier = Modifier,
    elemento: ElementoLista
) {
    val colorFondo = parseColor(elemento.colorString)
    val colorTexto = if (colorFondo == Color.Black || colorFondo == Color.Blue || colorFondo == Color.Red) Color.White else Color.Black

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = colorFondo,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = elemento.nombre, color = colorTexto)
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        InicioScreen()
    }
}
