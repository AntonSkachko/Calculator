package com.anton.calculator.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anton.calculator.data.Datasource
import com.anton.calculator.data.removeElement
import com.anton.calculator.data.solveExpression
import com.anton.calculator.models.CalcButton
import com.anton.calculator.ui.theme.CalculatorTheme


@Composable
fun CalculatorApp(
    modifier: Modifier = Modifier
) {
    var expression by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    var history by rememberSaveable { mutableStateOf(mutableListOf<String>()) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        CalculatorScreen(
            expression = expression,
            result = result,
            history = history,
            modifier = Modifier
                .weight(Datasource().takeWeightForScreen())
        )
        CalculatorKeyboard(
            onSymbolClick = { expression += it },
            onACClick = {
                expression = ""
                result = ""
            },
            onDELClick = {
                expression = removeElement(it)
            },
            onEqualClick = {

                if (expression.isEmpty()) {
                    return@CalculatorKeyboard
                }
                result = solveExpression(it)
                history.add("$it = $result")
                if (history.size > 3) {
                    history.removeFirst()
                }
            },
            elements = Datasource().takeButtonList(),
            expression = expression,
            modifier = Modifier
                .weight(Datasource().takeWeightForKeyboard())
        )
    }
}

@Composable
fun CalculatorKeyboard(
    modifier: Modifier = Modifier,
    onSymbolClick: (String) -> Unit,
    onACClick: (String) -> Unit,
    onDELClick: (String) -> Unit,
    onEqualClick: (String) -> Unit,
    elements: List<CalcButton>,
    expression: String
) {

    var prevElementIsFunList by rememberSaveable { mutableStateOf(mutableListOf(false)) }
    var prevElementIsDot by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        for (j in 0..3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                for (i in 0 until elements.size / 4) {
                    val temp = elements[j * (elements.size / 4) + i]
                    CalculatorButton(
                        symbol = temp.symbol,
                        expression = expression,
                        onClick = {
                            if ("." == temp.symbol && prevElementIsDot.not()) {
                                onSymbolClick(temp.element)
                                prevElementIsDot = true
                                prevElementIsFunList.add(temp.isFunction)
                            } else if (
                                (prevElementIsFunList.last().not() || temp.isFunction.not()) &&
                                ("." == temp.symbol).not()
                            ) {
                                onSymbolClick(temp.element)
                                if (temp.isFunction) {
                                    prevElementIsDot = false
                                }
                                prevElementIsFunList.add(temp.isFunction)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            CalculatorButton(
                symbol = "AC",
                expression = expression,
                onClick = {
                    prevElementIsFunList = mutableListOf(false)
                    onACClick(expression)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            CalculatorButton(
                symbol = "DEL",
                expression = expression,
                onClick = {
                    if (prevElementIsFunList.size > 1) {
                        if (prevElementIsFunList.last()) {
                            prevElementIsDot = true
                        }
                        prevElementIsFunList.removeLast()
                    }
                    onDELClick(expression)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                CalculatorButton(
                    symbol = "PI",
                    expression = expression,
                    onClick = {
                        onSymbolClick("PI")
                        prevElementIsFunList.add(false)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            CalculatorButton(
                symbol = "=",
                expression = expression,
                onClick = {
                    onEqualClick(expression)
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            )

        }
    }

}

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    expression: String,
    history: List<String>,
    result: String
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Text(
                text = history.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n"),
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
                lineHeight = 20.sp
            )
        }
        Text(
            text = expression,
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            lineHeight = 40.sp,

            )
        Text(
            text = " $result",
            fontSize = 48.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun CalculatorButton(
    modifier: Modifier = Modifier,
    expression: String,
    onClick: (String) -> Unit,
    symbol: String
) {
    Button(
        onClick = {
            onClick(expression)
        },
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium,
    ) {

        Text(
            text = symbol,
            fontSize = 25.sp
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}