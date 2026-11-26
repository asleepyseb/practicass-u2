package com.example.practicas_u2.Model

data class Taller(
    val nombre: String,
    val cupoTotal: Int,
    val inscritos: Int
) {
    val lugaresDisponibles: Int
        get() = (cupoTotal - inscritos).coerceAtLeast(0)
}

data class InscripcionUiState(
    val nombre: String = "",
    val correo: String = "",
    val edadTexto: String = "",
    val tallerSeleccionado: String = "Pintura",
    val aceptaAviso: Boolean = false,
    val intentoEnviar: Boolean = false,
    val inscrito: Boolean = false,
    val talleres: List<Taller> = listOf(
        Taller("Pintura", cupoTotal = 15, inscritos = 11),
        Taller("Fotografía", cupoTotal = 12, inscritos = 6),
        Taller("Guitarra", cupoTotal = 10, inscritos = 9)
    )
) {
    val tallerActual: Taller?
        get() = talleres.find { it.nombre == tallerSeleccionado }

    val errorNombre: String?
        get() = if (!intentoEnviar) null else validarNombre(nombre)

    val errorCorreo: String?
        get() = if (!intentoEnviar) null else validarCorreo(correo)

    val errorEdad: String?
        get() = if (!intentoEnviar) null else validarEdad(edadTexto)

    val errorAviso: String?
        get() = if (!intentoEnviar || aceptaAviso) null else "Debes aceptar el aviso de privacidad"

    val formularioValido: Boolean
        get() = validarNombre(nombre) == null &&
                validarCorreo(correo) == null &&
                validarEdad(edadTexto) == null &&
                aceptaAviso

    companion object {
        fun validarNombre(valor: String): String? {
            return if (valor.trim().length < 3) {
                "El nombre debe tener al menos 3 caracteres"
            } else {
                null
            }
        }

        fun validarCorreo(valor: String): String? {
            val limpio = valor.trim()
            val arrobaIndex = limpio.indexOf('@')
            val tienePuntoDespues = arrobaIndex > 0 && limpio.indexOf('.', startIndex = arrobaIndex + 1) > arrobaIndex + 1
            return if (!tienePuntoDespues || limpio.contains(' ')) {
                "Ingresa un correo válido"
            } else {
                null
            }
        }

        fun validarEdad(valor: String): String? {
            val edad = valor.toIntOrNull()
            return if (edad == null || edad !in 12..99) {
                "Ingresa una edad entre 12 y 99 años"
            } else {
                null
            }
        }
    }
}