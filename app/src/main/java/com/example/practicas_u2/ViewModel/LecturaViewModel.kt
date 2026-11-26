package com.example.practicas_u2.ViewModel

import androidx.lifecycle.ViewModel
import com.example.practicas_u2.Model.AjustesLectura
import com.example.practicas_u2.Model.Interlineado
import com.example.practicas_u2.Model.LecturaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LecturaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LecturaUiState())
    val uiState: StateFlow<LecturaUiState> = _uiState.asStateFlow()

    fun onTamanoChange(nuevoTamano: Float) {
        _uiState.update { state ->
            state.copy(ajustes = state.ajustes.copy(tamanoSp = nuevoTamano))
        }
    }

    fun onInterlineadoChange(nuevoInterlineado: Interlineado) {
        _uiState.update { state ->
            state.copy(ajustes = state.ajustes.copy(interlineado = nuevoInterlineado))
        }
    }

    fun onModoOscuroChange(activado: Boolean) {
        _uiState.update { state ->
            state.copy(ajustes = state.ajustes.copy(modoOscuro = activado))
        }
    }

    fun onUsaSerifChange(usarSerif: Boolean) {
        _uiState.update { state ->
            state.copy(ajustes = state.ajustes.copy(usaSerif = usarSerif))
        }
    }

    fun onRestablecer() {
        _uiState.update { state ->
            state.copy(ajustes = AjustesLectura())
        }
    }
}