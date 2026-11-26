package com.example.practicas_u2.ViewModel

import androidx.lifecycle.ViewModel
import com.example.practicas_u2.Model.AforoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AforoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AforoUiState())
    val uiState: StateFlow<AforoUiState> = _uiState.asStateFlow()

    fun onEntra() {
        _uiState.update { state ->
            if (state.actuales < state.maximo) {
                state.copy(
                    actuales = state.actuales + 1,
                    visitasDelDia = state.visitasDelDia + 1
                )
            } else {
                state
            }
        }
    }

    fun onEntraGrupo() {
        _uiState.update { state ->
            val espacioLibre = (state.maximo - state.actuales).coerceAtLeast(0)
            val ingresan = minOf(5, espacioLibre)
            state.copy(
                actuales = state.actuales + ingresan,
                visitasDelDia = state.visitasDelDia + ingresan
            )
        }
    }

    fun onSale() {
        _uiState.update { state ->
            if (state.actuales > 0) {
                state.copy(actuales = state.actuales - 1)
            } else {
                state
            }
        }
    }

    fun onCerrarDia() {
        _uiState.update { state ->
            state.copy(
                actuales = 0,
                visitasDelDia = 0
            )
        }
    }
}