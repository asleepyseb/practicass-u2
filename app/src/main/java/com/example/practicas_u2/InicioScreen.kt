package com.example.practicas_u2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove

data class Producto(val nombre: String, val precio: Double)

@Composable
fun CarritoScreen(modifier: Modifier = Modifier) {
    val catalogo = remember {
        listOf(
            Producto("Tortillas 1 kg", 24.00),
            Producto("Leche 1 L", 27.50),
            Producto("Huevo 12 pz", 52.00),
            Producto("Frijol 1 kg", 30.00),
        )
    }

    val cantidades = remember { mutableStateListOf(0,0,0,0) }

    val articulos = cantidades.sum()
    val subtotal = catalogo.indices.sumOf { catalogo[it].precio * cantidades[it] }
    val descuento = if(subtotal >=200.0) subtotal * 0.10 else 0.0
    val subtotalConDescuento = subtotal - descuento
    val envio = if(articulos== 0) 0.0 else if(subtotalConDescuento >= 150.00) 0.0 else 25.0
    val total = subtotalConDescuento + envio

    Column(modifier = modifier.padding(16.dp).fillMaxSize()) {
        Text("Tienda Don Pepe", style= MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.padding(16.dp))

        catalogo.forEachIndexed { index, producto -> ProductoItem(
            producto = producto,
            cantidad = cantidades[index],
            onCantidadChange = { nuevaCantidad -> cantidades[index] = nuevaCantidad.coerceIn(0,10)
            }
        )
        HorizontalDivider()
        }

        ResumenCarrito(
            articulos = articulos,
            subtotal = subtotal,
            descuento = descuento,
            envio = envio,
            total = total
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    for (i in cantidades.indices) cantidades[i] = 0
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Vaciar")
            }

            Button(
                onClick = { /* Lógica de confirmación */ },
                enabled = articulos > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text("Confirmar pedido")
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, cantidad: Int, onCantidadChange: (Int) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            Text(producto.nombre, fontWeight = FontWeight.Bold)
            Text("$${"%.2f".format(producto.precio)} c/u", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onCantidadChange(cantidad - 1) }) {
                Icon(Icons.Default.Remove, contentDescription = "Menos")
            }
            Text("$cantidad", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onCantidadChange(cantidad + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Más")
            }
        }
    }
}

@Composable
fun ResumenCarrito(articulos: Int, subtotal: Double, descuento: Double, envio: Double, total: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FilaResumen("Artículos", "$articulos")
        FilaResumen("Subtotal", "$${"%.2f".format(subtotal)}")
        FilaResumen("Descuento 10 %", "-$${"%.2f".format(descuento)}", Color(0xFF388E3C))
        FilaResumen("Envío", if (envio == 0.0) "GRATIS" else "$${"%.2f".format(envio)}", if (envio == 0.0) Color(0xFF388E3C) else Color.Unspecified)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("TOTAL", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun FilaResumen(etiqueta: String, valor: String, colorValor: Color = Color.Unspecified) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
        Text(valor, style = MaterialTheme.typography.bodyMedium, color = colorValor, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    CarritoScreen()
}