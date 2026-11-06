package com.example.practicas_u2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicas_u2.Components.Boton
import com.example.practicas_u2.ui.theme.Practicasu2Theme

@Composable
fun SiglasScreen(modifier: Modifier = Modifier) {
    var cant by remember { mutableStateOf("") }
    var repeticiones by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ingrese la cantidad de N")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = cant, onValueChange = { cant = it })
        Spacer(modifier = Modifier.height(16.dp))
        Boton(text = "Imprimir", onClick = { repeticiones = cant.toIntOrNull() ?: 0 })
        for (i in 1..repeticiones) {
            Text(text = "N de Niño")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SiglasScreenPreview() {
    Practicasu2Theme {
        SiglasScreen()
    }
}
