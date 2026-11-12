package com.example.practicas_u2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MiCard(
    modifier: Modifier = Modifier,
    nombre: String,
    onModificar: () -> Unit,
    onEliminar: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp),
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = nombre)
            Row {
                Button(onClick = onModificar) {
                    Text("Modificar")
                }
                Button(onClick = onEliminar) {
                    Text("Eliminar")
                }
            }
        }
    }
}