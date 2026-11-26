package com.example.practicas_u2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicas_u2.Model.AforoUiState
import com.example.practicas_u2.ViewModel.AforoViewModel

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    viewModel: AforoViewModel = viewModel()
) {
    AforoRoute(
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
fun AforoRoute(
    modifier: Modifier = Modifier,
    viewModel: AforoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AforoScreen(
        uiState = uiState,
        onEntra = viewModel::onEntra,
        onEntraGrupo = viewModel::onEntraGrupo,
        onSale = viewModel::onSale,
        onCerrarDia = viewModel::onCerrarDia,
        modifier = modifier
    )
}

@Composable
fun AforoScreen(
    uiState: AforoUiState,
    onEntra: () -> Unit,
    onEntraGrupo: () -> Unit,
    onSale: () -> Unit,
    onCerrarDia: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Biblioteca Central",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sala de estudio · capacidad ${uiState.maximo}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AforoIndicator(
                actuales = uiState.actuales,
                maximo = uiState.maximo,
                porcentaje = uiState.porcentaje,
                porcentajeEntero = uiState.porcentajeEntero,
                colorIndicador = uiState.colorIndicador,
                textoEstado = uiState.textoEstado
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onSale,
                    enabled = uiState.puedeSalir,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("- Sale")
                }
                Button(
                    onClick = onEntra,
                    enabled = uiState.puedeEntrar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Entra")
                }
            }

            OutlinedButton(
                onClick = onEntraGrupo,
                enabled = uiState.puedeEntrar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+5 (grupo)")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Visitas del día",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Solo suma las entradas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = uiState.visitasDelDia.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LeyendaAforo()
        }

        OutlinedButton(
            onClick = onCerrarDia,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar día")
        }
    }
}

@Composable
fun AforoIndicator(
    actuales: Int,
    maximo: Int,
    porcentaje: Float,
    porcentajeEntero: Int,
    colorIndicador: Color,
    textoEstado: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorIndicador.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$actuales / $maximo",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = colorIndicador
            )
            Text(
                text = "personas en la sala",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { porcentaje.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = colorIndicador
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$porcentajeEntero % de ocupación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorIndicador
            )
            Text(
                text = textoEstado,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun LeyendaAforo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemLeyenda(color = Color(0xFF1B7A2B), texto = "Verde < 70 %")
        ItemLeyenda(color = Color(0xFFC78500), texto = "Amarillo 70–99 %")
        ItemLeyenda(color = Color(0xFFB3261E), texto = "Rojo 100 %")
    }
}

@Composable
private fun ItemLeyenda(color: Color, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AforoScreenPreview() {
    Surface {
        AforoScreen(
            uiState = AforoUiState(
                actuales = 32,
                visitasDelDia = 118,
                maximo = 50
            ),
            onEntra = {},
            onEntraGrupo = {},
            onSale = {},
            onCerrarDia = {}
        )
    }
}