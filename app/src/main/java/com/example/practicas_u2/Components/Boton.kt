package com.example.practicas_u2.Components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Boton(
    modifier: Modifier = Modifier,
    text: String = "Mi Botón",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}
