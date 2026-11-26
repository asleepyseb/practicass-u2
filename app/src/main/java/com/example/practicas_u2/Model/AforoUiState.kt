package com.example.practicas_u2.Model

import androidx.compose.ui.graphics.Color

data class AforoUiState(
    val actuales: Int = 0,
    val visitasDelDia: Int = 0,
    val maximo: Int = 50
) {
    val porcentaje: Float
        get() = if (maximo > 0) actuales / maximo.toFloat() else 0f

    val porcentajeEntero: Int
        get() = (porcentaje * 100).toInt()

    val estaLlena: Boolean
        get() = actuales >= maximo

    val puedeEntrar: Boolean
        get() = actuales < maximo

    val puedeSalir: Boolean
        get() = actuales > 0

    val colorIndicador: Color
        get() = when {
            porcentajeEntero < 70 -> Color(0xFF1B7A2B)
            porcentajeEntero < 100 -> Color(0xFFC78500)
            else -> Color(0xFFB3261E)
        }

    val textoEstado: String
        get() = when {
            porcentajeEntero < 70 -> "Aforo disponible"
            porcentajeEntero < 100 -> "Aforo alto"
            else -> "Capacidad máxima alcanzada"
        }
}