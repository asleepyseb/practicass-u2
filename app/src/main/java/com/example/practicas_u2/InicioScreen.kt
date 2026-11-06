package com.example.practicas_u2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicas_u2.Components.Boton
import com.example.practicas_u2.ui.theme.Practicasu2Theme

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Boton(
            text = "Presióname",
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        InicioScreen()
    }
}
