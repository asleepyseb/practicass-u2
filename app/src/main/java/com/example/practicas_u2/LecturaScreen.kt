package com.example.practicas_u2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicas_u2.Model.AjustesLectura
import com.example.practicas_u2.Model.Interlineado
import com.example.practicas_u2.Model.LecturaUiState
import com.example.practicas_u2.ViewModel.LecturaViewModel

@Composable
fun LecturaRoute(
    modifier: Modifier = Modifier,
    viewModel: LecturaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LecturaScreen(
        uiState = uiState,
        onTamanoChange = viewModel::onTamanoChange,
        onInterlineadoChange = viewModel::onInterlineadoChange,
        onModoOscuroChange = viewModel::onModoOscuroChange,
        onUsaSerifChange = viewModel::onUsaSerifChange,
        onRestablecer = viewModel::onRestablecer,
        modifier = modifier
    )
}

@Composable
fun LecturaScreen(
    uiState: LecturaUiState,
    onTamanoChange: (Float) -> Unit,
    onInterlineadoChange: (Interlineado) -> Unit,
    onModoOscuroChange: (Boolean) -> Unit,
    onUsaSerifChange: (Boolean) -> Unit,
    onRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ajustes de lectura",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        ControlesLectura(
            ajustes = uiState.ajustes,
            onTamanoChange = onTamanoChange,
            onInterlineadoChange = onInterlineadoChange,
            onModoOscuroChange = onModoOscuroChange,
            onUsaSerifChange = onUsaSerifChange
        )

        HorizontalDivider()

        Text(
            text = "VISTA PREVIA",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VistaPrevia(
            texto = uiState.textoEjemplo,
            ajustes = uiState.ajustes
        )

        OutlinedButton(
            onClick = onRestablecer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restablecer")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ControlesLectura(
    ajustes: AjustesLectura,
    onTamanoChange: (Float) -> Unit,
    onInterlineadoChange: (Interlineado) -> Unit,
    onModoOscuroChange: (Boolean) -> Unit,
    onUsaSerifChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tamaño de letra",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${ajustes.tamanoSp.toInt()} sp",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = ajustes.tamanoSp,
                onValueChange = onTamanoChange,
                valueRange = 14f..32f
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("A", fontSize = 14.sp)
                Text("A", fontSize = 24.sp)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Interlineado",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Interlineado.entries.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = ajustes.interlineado == opcion,
                                onClick = { onInterlineadoChange(opcion) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ajustes.interlineado == opcion,
                            onClick = null
                        )
                        Text(
                            text = opcion.etiqueta,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modo oscuro",
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = ajustes.modoOscuro,
                onCheckedChange = onModoOscuroChange
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fuente",
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = ajustes.usaSerif,
                    onClick = { onUsaSerifChange(true) },
                    label = { Text("Serif") }
                )
                FilterChip(
                    selected = !ajustes.usaSerif,
                    onClick = { onUsaSerifChange(false) },
                    label = { Text("Sans") }
                )
            }
        }
    }
}

@Composable
fun VistaPrevia(
    texto: String,
    ajustes: AjustesLectura,
    modifier: Modifier = Modifier
) {
    val fondo = if (ajustes.modoOscuro) Color(0xFF1E1B1E) else Color(0xFFF7F4F9)
    val colorTexto = if (ajustes.modoOscuro) Color(0xFFF3EFF4) else Color(0xFF1C1B1F)
    val familiaFuente = if (ajustes.usaSerif) FontFamily.Serif else FontFamily.SansSerif
    val tamano = ajustes.tamanoSp.sp
    val alturaLinea = (ajustes.tamanoSp * ajustes.interlineado.factor).sp

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = fondo)
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontFamily = familiaFuente,
            fontSize = tamano,
            lineHeight = alturaLinea,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LecturaScreenPreview() {
    Surface {
        LecturaScreen(
            uiState = LecturaUiState(
                ajustes = AjustesLectura(
                    tamanoSp = 22f,
                    interlineado = Interlineado.NORMAL,
                    modoOscuro = true,
                    usaSerif = true
                )
            ),
            onTamanoChange = {},
            onInterlineadoChange = {},
            onModoOscuroChange = {},
            onUsaSerifChange = {},
            onRestablecer = {}
        )
    }
}