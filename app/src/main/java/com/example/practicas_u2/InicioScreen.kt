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
import androidx.compose.ui.unit.sp
import com.example.practicas_u2.Components.Boton_Suma
import com.example.practicas_u2.ui.theme.Practicasu2Theme

@Composable
fun SumaScreen(modifier: Modifier = Modifier) {
    var val1 by remember { mutableStateOf("") }
    var val2 by remember { mutableStateOf("") }
    var suma by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = val1,
            onValueChange = { val1 = it },
            label = { Text("Valor 1") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = val2,
            onValueChange = { val2 = it },
            label = { Text("Valor 2") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$suma", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Boton_Suma(
            text = "Sumar",
            onClick = { suma = (val1.toIntOrNull() ?: 0) + (val2.toIntOrNull() ?: 0) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SumaScreenPreview() {
    Practicasu2Theme {
        SumaScreen()
    }
}
