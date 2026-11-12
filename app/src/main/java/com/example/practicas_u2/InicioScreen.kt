package com.example.practicas_u2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var listaNombres by rememberSaveable { mutableStateOf(listOf<String>()) }
    var nombreEscrito by rememberSaveable { mutableStateOf("") }
    var indiceEditando by rememberSaveable { mutableStateOf<Int?>(null) }

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
                            if (indiceEditando != null) {
                                val mutableList = listaNombres.toMutableList()
                                mutableList[indiceEditando!!] = nombreEscrito
                                listaNombres = mutableList
                                indiceEditando = null
                            } else {
                                listaNombres = listaNombres + nombreEscrito
                            }
                            nombreEscrito = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (indiceEditando != null) "Guardar Cambios" else "Agregar")
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        ) {
            itemsIndexed(listaNombres) { indice, nombre ->
                MiCard(
                    nombre = nombre,
                    onModificar = {
                        nombreEscrito = nombre
                        indiceEditando = indice
                    },
                    onEliminar = {
                        val mutableList = listaNombres.toMutableList()
                        mutableList.removeAt(indice)
                        listaNombres = mutableList
                        if (indiceEditando == indice) {
                            nombreEscrito = ""
                            indiceEditando = null
                        } else if (indiceEditando != null && indiceEditando!! > indice) {
                            indiceEditando = indiceEditando!! - 1
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        InicioScreen()
    }
}