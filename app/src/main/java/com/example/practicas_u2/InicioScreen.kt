package com.example.practicas_u2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicas_u2.Components.MiCard

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {


    var textoIngresado by remember { mutableStateOf("") }

    val listaNombres = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = textoIngresado,
            onValueChange = { nuevoTexto ->
                textoIngresado = nuevoTexto
            },
            label = { Text("Ingresa un nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (textoIngresado.isNotBlank()) {
                    listaNombres.add(textoIngresado)
                    textoIngresado = ""
                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Agregar a la lista")
        }


        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaNombres) { nombre ->
                MiCard(nombre = nombre)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InicioPreview() {
    InicioScreen()
}