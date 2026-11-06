package com.example.practicas_u2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicas_u2.Components.Boton
import com.example.practicas_u2.ui.theme.Practicasu2Theme

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var contador by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Contador: $contador",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Boton(
            text = "Sumar 1",
            onClick = { contador++ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        InicioScreen()
    }
}
