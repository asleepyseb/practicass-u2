package com.example.practicas_u2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import kotlin.math.ceil
import  androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

// 1. Pantalla Padre (Dueña del estado y la lógica)
@Composable
fun CuentaScreen(modifier: Modifier = Modifier) {
    // Estados base
    var montoTexto by remember { mutableStateOf("") }
    var propinaPct by remember { mutableStateOf(20) }
    var usaOtro by remember { mutableStateOf(false) }
    var personas by remember { mutableStateOf(5) }
    var redondear by remember { mutableStateOf(false) }

    // Cálculos derivados (No son estado guardado, se recalculan solos)
    val monto = montoTexto.toDoubleOrNull() ?: 0.0 // Manejo seguro de nulos
    val propinaTotal = monto * (propinaPct / 100.0)
    val totalConPropina = monto + propinaTotal

    val porPersonaExacto = if (personas > 0) totalConPropina / personas else 0.0
    val porPersona = if (redondear) ceil(porPersonaExacto) else porPersonaExacto
    val sobrante = if (redondear) (porPersona * personas) - totalConPropina else 0.0

    Column(modifier = modifier.padding(16.dp).fillMaxSize()) {
        Text("Cuenta dividida", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = montoTexto,
            onValueChange = { montoTexto = it },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = { Text("$", modifier = Modifier.padding(start = 8.dp)) },
            modifier = Modifier.fillMaxWidth(),
            isError = montoTexto.isNotEmpty() && montoTexto.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        SelectorPropina(
            porcentaje = propinaPct,
            usaOtro = usaOtro,
            onPorcentajeChange = { propinaPct = it },
            onOtroChange = { usaOtro = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StepperPersonas(
            personas = personas,
            onChange = { personas = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Redondear por persona")
            Switch(checked = redondear, onCheckedChange = { redondear = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        ResultadoCuenta(
            propinaPct = propinaPct,
            propina = propinaTotal,
            total = totalConPropina,
            porPersona = porPersona,
            personas = personas,
            sobrante = sobrante
        )
    }
}

// --- Composables "Stateless" ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorPropina(
    porcentaje: Int, usaOtro: Boolean,
    onPorcentajeChange: (Int) -> Unit, onOtroChange: (Boolean) -> Unit
) {
    Column {
        Text("Propina")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(10, 15, 20).forEach { pct ->
                FilterChip(
                    selected = !usaOtro && porcentaje == pct,
                    onClick = {
                        onOtroChange(false)
                        onPorcentajeChange(pct)
                    },
                    label = { Text("$pct %") }
                )
            }
            FilterChip(
                selected = usaOtro,
                onClick = { onOtroChange(true) },
                label = { Text("Otro") }
            )
        }
        if (usaOtro) {
            Slider(
                value = porcentaje.toFloat(),
                onValueChange = { onPorcentajeChange(it.toInt()) },
                valueRange = 0f..30f
            )
            Text("Seleccionado: $porcentaje %", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun StepperPersonas(personas: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Personas")
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onChange((personas - 1).coerceAtLeast(1)) }) {
                Icon(Icons.Default.Remove, contentDescription = "Menos")
            }
            Text("$personas", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            IconButton(onClick = { onChange((personas + 1).coerceAtMost(20)) }) {
                Icon(Icons.Default.Add, contentDescription = "Más")
            }
        }
    }
}

@Composable
fun ResultadoCuenta(
    propinaPct: Int, propina: Double, total: Double,
    porPersona: Double, personas: Int, sobrante: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Propina ($propinaPct %)")
                Text("$${"%.2f".format(propina)}", fontWeight = FontWeight.Bold)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total con propina")
                Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Cada quien paga")
            Text(
                text = "$${"%.2f".format(porPersona)}",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text("entre $personas personas", style = MaterialTheme.typography.bodyMedium)

            if (sobrante > 0) {
                Text(
                    text = "Sobrante por redondeo: $${"%.2f".format(sobrante)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    CuentaScreen()
}