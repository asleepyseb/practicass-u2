package com.example.practicas_u2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicas_u2.ui.theme.Practicasu2Theme

@Composable
fun AforoScreen(modifier: Modifier = Modifier) {
    val aforoMaximo = 50

    //variables mutables
    var personasActuales by remember { mutableStateOf(0) }
    var visitasDelDia by remember { mutableStateOf(0) }

    val porcentaje = personasActuales / aforoMaximo.toFloat()

    val colorIndicador = when {
        porcentaje < 0.7f -> Color.Green
        porcentaje < 1f -> Color.Yellow
        else -> Color.Red
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Biblioteca Central", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Sala de estudio - Capacidad $aforoMaximo")

        Spacer(modifier = Modifier.height(16.dp))

        //tarjeta de aforo

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)){
                Text(
                    text = "$personasActuales / $aforoMaximo",
                    style = MaterialTheme.typography.displayLarge,
                    color = colorIndicador
                )
                Text(text = "personas en la sala")

                LinearProgressIndicator(
                    progress = { porcentaje },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    color = colorIndicador

                )

                Text( text = "${(porcentaje * 100).toInt()} % de ocupacion")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
            Button(
                onClick = { personasActuales-- },
                enabled = personasActuales > 0
            ){
                Text("- Sale")
            }

            Button(
                onClick = {
                    personasActuales++
                    visitasDelDia++
                },
                enabled = personasActuales < aforoMaximo
            ){
                Text("+ Entra")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "visitas del dia\nSolo suma las entradas")
                Text(text = "$visitasDelDia", style = MaterialTheme.typography.displayMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                personasActuales = 0
                visitasDelDia = 0
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar dia")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        AforoScreen()
    }
}