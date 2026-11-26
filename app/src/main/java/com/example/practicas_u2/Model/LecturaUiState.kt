package com.example.practicas_u2.Model

enum class Interlineado(val etiqueta: String, val factor: Float) {
    COMPACTO("Compacto", 1.0f),
    NORMAL("Normal", 1.3f),
    AMPLIO("Amplio", 1.6f)
}

data class AjustesLectura(
    val tamanoSp: Float = 18f,
    val interlineado: Interlineado = Interlineado.NORMAL,
    val modoOscuro: Boolean = false,
    val usaSerif: Boolean = false
)

data class LecturaUiState(
    val ajustes: AjustesLectura = AjustesLectura(),
    val textoEjemplo: String = "Era una noche tranquila en el pueblo cuando las campanas comenzaron a sonar, y nadie supo por qué."
)