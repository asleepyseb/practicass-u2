package com.example.inicio_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inicio_screen.Components.MiCard
import com.example.inicio_screen.ui.theme.Inicio_ScreenTheme

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var listaNombres by rememberSaveable { mutableStateOf(listOf<String>()) }
    var nombreEscrito by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = nombreEscrito,
                    onValueChange = { nombreEscrito = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre") },
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (nombreEscrito.isNotBlank()) {
                            listaNombres += nombreEscrito
                            nombreEscrito = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Agregar")
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        ) {
            items(listaNombres) { nombre ->
                MiCard(nombre = nombre)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Inicio_ScreenTheme {
        InicioScreen()
    }
}