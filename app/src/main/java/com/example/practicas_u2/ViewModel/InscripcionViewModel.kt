package com.example.practicas_u2.ViewModel

import androidx.lifecycle.ViewModel
import com.example.practicas_u2.Model.InscripcionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InscripcionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InscripcionUiState())
    val uiState: StateFlow<InscripcionUiState> = _uiState.asStateFlow()

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
    }

    fun onEdadChange(nuevaEdad: String) {
        val soloDigitos = nuevaEdad.filter { it.isDigit() }
        _uiState.update { it.copy(edadTexto = soloDigitos) }
    }

    fun onTallerChange(nuevoTaller: String) {
        _uiState.update { it.copy(tallerSeleccionado = nuevoTaller) }
    }

    fun onAceptaAvisoChange(aceptado: Boolean) {
        _uiState.update { it.copy(aceptaAviso = aceptado) }
    }

    fun onEnviarClick() {
        _uiState.update { state ->
            val conIntento = state.copy(intentoEnviar = true)
            if (conIntento.formularioValido) {
                val talleresActualizados = state.talleres.map { taller ->
                    if (taller.nombre == state.tallerSeleccionado && taller.lugaresDisponibles > 0) {
                        taller.copy(inscritos = taller.inscritos + 1)
                    } else {
                        taller
                    }
                }
                conIntento.copy(
                    talleres = talleresActualizados,
                    inscrito = true
                )
            } else {
                conIntento
            }
        }
    }

    fun onNuevaInscripcion() {
        _uiState.update { state ->
            InscripcionUiState(talleres = state.talleres)
        }
    }
}