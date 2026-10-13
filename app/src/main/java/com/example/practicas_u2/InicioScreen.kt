package com.example.practicas_u2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicas_u2.Components.BotonCalculadora
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.practicas_u2.ui.theme.Practicasu2Theme
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun CalculadoraScreen(modifier: Modifier = Modifier) {
    var textoPantalla by remember {mutableStateOf("0")}
    var numeroAnterior by remember {mutableStateOf(0)}
    var operacion by remember { mutableStateOf("")}
    var nuevaOp by remember {mutableStateOf(true)}

    fun onBotonClick(symbol: String){
        when(symbol){
            "C" -> {
                textoPantalla = "0"
                numeroAnterior = 0
                operacion = ""
                nuevaOp = true
            }
            "+", "-", "*", "/" ->{
                numeroAnterior = textoPantalla.toIntOrNull() ?:0
                operacion = symbol
                nuevaOp = true
            }
            "=" ->{
                val numeroActual = textoPantalla.toIntOrNull() ?:0
                val resultado = when(operacion){
                    "+"-> numeroAnterior + numeroActual
                    "-"-> numeroAnterior - numeroActual
                    "*"-> numeroAnterior * numeroActual
                    "/"-> if(numeroActual != 0) numeroAnterior / numeroActual else 0
                    else -> numeroActual
                }
                textoPantalla = if (resultado % 1 == 0) resultado.toInt().toString() else resultado.toString()
                operacion = ""
                nuevaOp = true
            }
            else -> {
                if(nuevaOp){
                    textoPantalla = symbol
                    nuevaOp = false
                } else {
                    if(textoPantalla == "0" && symbol != ".") textoPantalla = symbol
                    else textoPantalla += symbol
                }
            }
        }
}

    //diseño pantalla
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ){
        //referencias para los elementos
        val pantalla = createRef()
        val (btnC, btnDiv) = createRefs()
        val (btn7, btn8, btn9, btnMult) = createRefs()
        val (btn4, btn5, btn6, btnRes) = createRefs()
        val (btn1, btn2, btn3, btnSum) = createRefs()
        val (btn0, btnIgual) = createRefs()

        val g1 = createGuidelineFromStart(0.25f)
        val g2 = createGuidelineFromStart(0.5f)
        val g3 = createGuidelineFromStart(0.75f)

        //pantalla
        Text(
            text = textoPantalla,
            fontSize = 64.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .constrainAs(pantalla){
                    bottom.linkTo(btnC.top, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        //primera fila de botones
        BotonCalculadora("C", Modifier.constrainAs(btnC) {
            bottom.linkTo(btn7.top); start.linkTo(g2); end.linkTo(btnDiv.start); width = Dimension.fillToConstraints
        }.padding(4.dp), Color.LightGray, Color.Black) { onBotonClick("C") }

        BotonCalculadora("/", Modifier.constrainAs(btnDiv) {
            bottom.linkTo(btnMult.top); start.linkTo(g3); end.linkTo(parent.end); width = Dimension.fillToConstraints
        }.padding(4.dp), Color(0xFFFF9800), Color.White) { onBotonClick("/") }

        //fila dos
        BotonCalculadora("7", Modifier.constrainAs(btn7) {
            bottom.linkTo(btn4.top); start.linkTo(parent.start); end.linkTo(g1); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("7") }

        BotonCalculadora("8", Modifier.constrainAs(btn8) {
            bottom.linkTo(btn5.top); start.linkTo(g1); end.linkTo(g2); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("8") }

        BotonCalculadora("9", Modifier.constrainAs(btn9) {
            bottom.linkTo(btn6.top); start.linkTo(g2); end.linkTo(g3); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("9") }

        BotonCalculadora("*", Modifier.constrainAs(btnMult) {
            bottom.linkTo(btnRes.top); start.linkTo(g3); end.linkTo(parent.end); width = Dimension.fillToConstraints
        }.padding(4.dp), Color(0xFFFF9800), Color.White) { onBotonClick("*") }

        //fila tres
        BotonCalculadora("4", Modifier.constrainAs(btn4) {
            bottom.linkTo(btn1.top); start.linkTo(parent.start); end.linkTo(g1); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("4") }

        BotonCalculadora("5", Modifier.constrainAs(btn5) {
            bottom.linkTo(btn2.top); start.linkTo(g1); end.linkTo(g2); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("5") }

        BotonCalculadora("6", Modifier.constrainAs(btn6) {
            bottom.linkTo(btn3.top); start.linkTo(g2); end.linkTo(g3); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("6") }

        BotonCalculadora("-", Modifier.constrainAs(btnRes) {
            bottom.linkTo(btnSum.top); start.linkTo(g3); end.linkTo(parent.end); width = Dimension.fillToConstraints
        }.padding(4.dp), Color(0xFFFF9800), Color.White) { onBotonClick("-") }

        //fila cuatro
        BotonCalculadora("1", Modifier.constrainAs(btn1) {
            bottom.linkTo(btn0.top); start.linkTo(parent.start); end.linkTo(g1); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("1") }

        BotonCalculadora("2", Modifier.constrainAs(btn2) {
            bottom.linkTo(btn0.top); start.linkTo(g1); end.linkTo(g2); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("2") }

        BotonCalculadora("3", Modifier.constrainAs(btn3) {
            bottom.linkTo(btn0.top); start.linkTo(g2); end.linkTo(g3); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("3") }

        BotonCalculadora("+", Modifier.constrainAs(btnSum) {
            bottom.linkTo(btnIgual.top); start.linkTo(g3); end.linkTo(parent.end); width = Dimension.fillToConstraints
        }.padding(4.dp), Color(0xFFFF9800), Color.White) { onBotonClick("+") }

        //fila 5
        BotonCalculadora("0", Modifier.constrainAs(btn0) {
            bottom.linkTo(parent.bottom); start.linkTo(parent.start); end.linkTo(btnIgual.start); width = Dimension.fillToConstraints
        }.padding(4.dp)) { onBotonClick("0") }

        BotonCalculadora("=", Modifier.constrainAs(btnIgual) {
            bottom.linkTo(parent.bottom); start.linkTo(g3); end.linkTo(parent.end); width = Dimension.fillToConstraints
        }.padding(4.dp), Color(0xFFFF9800), Color.White) { onBotonClick("=") }
        }
    }

    /*Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = nombreEscrito,
                    onValueChange = { nombreEscrito = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre") },
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (nombreEscrito.isNotBlank()) {
                            listaNombres += nombreEscrito
                            nombreEscrito = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Agregar")
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        ) {
            items(listaNombres) { nombre ->
                Boton(nombre = nombre)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }*/


@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    Practicasu2Theme {
        CalculadoraScreen()
    }
}