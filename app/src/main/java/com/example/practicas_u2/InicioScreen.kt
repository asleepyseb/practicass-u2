package com.example.practicas_u2

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// 1. Modelos de datos
enum class Categoria { COMIDA, TRANSPORTE, RENTA, OCIO, OTROS }

data class Gasto(val id: Int, val concepto: String, val monto: Double, val categoria: Categoria)

// 2. Pantalla Padre (Controla el presupuesto global)
@Composable
fun PresupuestoScreen(modifier : Modifier = Modifier) {
    var ingresoTexto by remember { mutableStateOf("4500") }
    val gastos = remember { mutableStateListOf<Gasto>() }
    var idCounter by remember { mutableStateOf(0) }

    // Cálculos derivados
    val ingreso = ingresoTexto.toDoubleOrNull() ?: 0.0
    val totalGastado = gastos.sumOf { it.monto }
    val saldo = ingreso - totalGastado
    val porcentaje = if (ingreso > 0) (totalGastado / ingreso).toFloat() else 0f

    val gastoPorCategoria = gastos.groupBy { it.categoria }
        .mapValues { it.value.sumOf { g -> g.monto } }
    val categoriaTop = gastoPorCategoria.maxByOrNull { it.value }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("Mi presupuesto", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ingresoTexto,
            onValueChange = { ingresoTexto = it },
            label = { Text("Ingreso mensual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = { Text("$", modifier = Modifier.padding(start = 8.dp)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResumenPresupuesto(
            totalGastado = totalGastado,
            saldo = saldo,
            porcentaje = porcentaje,
            categoriaTopNombre = categoriaTop?.key?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
            categoriaTopMonto = categoriaTop?.value
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Nuevo gasto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        // El formulario recibe una lambda de "alto nivel", entregando un gasto completo
        FormularioGasto(
            onAgregar = { concepto, monto, categoria ->
                gastos.add(Gasto(idCounter++, concepto, monto, categoria))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Gastos recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(gastos.reversed(), key = { it.id }) { gasto ->
                GastoItem(gasto = gasto, onEliminar = { gastos.remove(gasto) })
            }
        }
    }
}

// 3. Formulario (Encapsula su propio estado de edición)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioGasto(onAgregar: (String, Double, Categoria) -> Unit) {
    // Estado local: ¡No se hoistearon porque al padre no le importan las teclas intermedias!
    var concepto by remember { mutableStateOf("") }
    var montoTexto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(Categoria.COMIDA) }
    var menuAbierto by remember { mutableStateOf(false) }

    val monto = montoTexto.toDoubleOrNull()
    val isValid = concepto.isNotBlank() && monto != null && monto > 0

    Column {
        OutlinedTextField(
            value = concepto,
            onValueChange = { concepto = it },
            label = { Text("Concepto") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = montoTexto,
                onValueChange = { montoTexto = it },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )

            ExposedDropdownMenuBox(
                expanded = menuAbierto,
                onExpandedChange = { menuAbierto = !menuAbierto },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = categoria.name.lowercase().replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    Categoria.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                categoria = cat
                                menuAbierto = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isValid) {
                    onAgregar(concepto, monto!!, categoria)
                    // Reiniciar el formulario local tras agregar
                    concepto = ""
                    montoTexto = ""
                    categoria = Categoria.COMIDA
                }
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar gasto")
        }
    }
}

// 4. Componentes Stateless
@Composable
fun ResumenPresupuesto(
    totalGastado: Double, saldo: Double, porcentaje: Float,
    categoriaTopNombre: String?, categoriaTopMonto: Double?
) {
    val colorAlerta = when {
        porcentaje >= 1f -> Color(0xFFFFCDD2) // Rojo
        porcentaje >= 0.8f -> Color(0xFFFFF9C4) // Amarillo
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(colors = CardDefaults.cardColors(containerColor = colorAlerta)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Gastado", style = MaterialTheme.typography.labelMedium)
                    Text("$${"%.2f".format(totalGastado)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Saldo", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "$${"%.2f".format(saldo)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (saldo < 0) Color.Red else Color(0xFF388E3C)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { porcentaje.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                color = if (porcentaje >= 1f) Color.Red else if (porcentaje >= 0.8f) Color(0xFFFBC02D) else MaterialTheme.colorScheme.primary
            )

            val infoExtra = if (categoriaTopNombre != null) {
                "${(porcentaje * 100).toInt()}% usado · Mayor gasto: $categoriaTopNombre ($${"%.2f".format(categoriaTopMonto)})"
            } else {
                "Sin gastos registrados"
            }
            Text(infoExtra, style = MaterialTheme.typography.bodySmall)

            if (porcentaje >= 0.8f) {
                Spacer(modifier = Modifier.height(8.dp))
                val mensaje = if (porcentaje >= 1f) "Te has excedido del presupuesto" else "Cuidado: pasaste el 80 %"
                Text("⚠️ $mensaje", color = if (porcentaje >= 1f) Color.Red else Color(0xFFF57F17), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GastoItem(gasto: Gasto, onEliminar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(gasto.concepto, fontWeight = FontWeight.Bold)
            Text(gasto.categoria.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodySmall)
        }
        Text("-$${"%.2f".format(gasto.monto)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
        IconButton(onClick = onEliminar) {
            Icon(Icons.Default.Close, contentDescription = "Eliminar gasto")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    PresupuestoScreen()
}