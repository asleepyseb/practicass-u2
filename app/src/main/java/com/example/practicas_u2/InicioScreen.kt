package com.example.practicas_u2

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Estados de pantalla posibles
enum class LoginEstado { INACTIVO, ERROR, BLOQUEADO, EXITO }

// 2. Pantalla Principal (El Controlador Central)
@Composable
fun LoginScreen(modifier : Modifier = Modifier) {
    // Estos estados DEBEN estar aquí porque dictan la lógica de negocio y navegación
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var recordarme by remember { mutableStateOf(false) }

    var intentosFallidos by remember { mutableStateOf(0) }
    var estado by remember { mutableStateOf(LoginEstado.INACTIVO) }

    // Flujo de navegación simulada
    when (estado) {
        LoginEstado.EXITO -> {
            HomeScreen(
                email = email,
                onLogout = {
                    email = ""
                    password = ""
                    recordarme = false
                    intentosFallidos = 0
                    estado = LoginEstado.INACTIVO
                }
            )
        }
        else -> {
            // Generación de mensajes dinámicos
            val mensajeError = when (estado) {
                LoginEstado.ERROR -> "Correo o contraseña incorrectos (intento $intentosFallidos de 3)"
                LoginEstado.BLOQUEADO -> "Cuenta bloqueada por múltiples intentos. Intenta más tarde."
                else -> null
            }

            val bloqueado = estado == LoginEstado.BLOQUEADO

            LoginForm(
                email = email,
                onEmailChange = { email = it; if (estado == LoginEstado.ERROR) estado = LoginEstado.INACTIVO },
                password = password,
                onPasswordChange = { password = it; if (estado == LoginEstado.ERROR) estado = LoginEstado.INACTIVO },
                recordarme = recordarme,
                onRecordarmeChange = { recordarme = it },
                mensajeError = mensajeError,
                bloqueado = bloqueado,
                onLogin = {
                    if (email == "paciente@vida.mx" && password == "Vida2025") {
                        estado = LoginEstado.EXITO
                        intentosFallidos = 0 // Reinicio de seguridad
                    } else {
                        intentosFallidos++
                        if (intentosFallidos >= 3) {
                            estado = LoginEstado.BLOQUEADO
                        } else {
                            estado = LoginEstado.ERROR
                        }
                    }
                }
            )
        }
    }
}

// 3. Componente LoginForm (Stateless en lógica de negocio, pero con estado de UI encapsulado)
@Composable
fun LoginForm(
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    recordarme: Boolean, onRecordarmeChange: (Boolean) -> Unit,
    mensajeError: String?, bloqueado: Boolean, onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Encabezado
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("+", fontSize = 48.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Clínica Vida", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("Consulta tus citas médicas", color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))

        // Campos de texto
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !bloqueado
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Uso del composable extra que maneja su propio estado
        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            enabled = !bloqueado
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = recordarme, onCheckedChange = onRecordarmeChange, enabled = !bloqueado)
            Text("Recordarme")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error/bloqueo
        if (mensajeError != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !bloqueado && email.isNotBlank() && password.isNotBlank()
        ) {
            Text("Entrar")
        }

        TextButton(onClick = { /* Acción de recuperación */ }, modifier = Modifier.padding(top = 16.dp)) {
            Text("¿Olvidaste tu contraseña?")
        }
    }
}

// Reto Extra: Composable PasswordField que gestiona su propio estado
@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit, enabled: Boolean = true) {
    /*
       ¿Por qué NO hoistear mostrarPassword?
       Porque a la pantalla padre (LoginScreen) NO le importa si la contraseña
       se está dibujando con asteriscos o con letras normales. La lógica de negocio
       (el intento de login) funciona igual. Es un detalle puramente visual y temporal.
    */
    var mostrarPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            val icon = if (mostrarPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        }
    )
}

// 4. Pantalla Secundaria (Simulada)
@Composable
fun HomeScreen(email: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Bienvenido!", style = MaterialTheme.typography.headlineMedium)
        Text(email)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogout) {
            Text("Cerrar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LecturaScreenPreview() {
    LoginScreen()
}