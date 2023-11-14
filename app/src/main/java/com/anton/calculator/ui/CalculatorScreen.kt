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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anton.calculator.models.CalcButton
import com.anton.calculator.ui.theme.CalculatorTheme

@Composable
fun CalculatorApp(
    modifier: Modifier = Modifier,
    calculatorViewModel: CalculatorViewModel = viewModel()
) {

    val calculatorUiState by calculatorViewModel.uiState.collectAsState()
    val orientation = LocalConfiguration.current.orientation
    val screenConfiguration = calculatorViewModel.getScreenConfiguration(orientation)

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        CalculatorScreen(
            expression = calculatorUiState.expression,
            result = calculatorUiState.result,
            history = calculatorUiState.history,
            modifier = Modifier
                .weight(screenConfiguration.weightForScreen)
        )
        CalculatorKeyboard(
            onSymbolClick = { calculatorViewModel.onDefaultButtonClick(it) },
            onACClick = { calculatorViewModel.onACButtonClick() },
            onDELClick = { calculatorViewModel.onRemoveButtonClick() },
            onEqualClick = {calculatorViewModel.onEqualButtonClick() },
            elements = screenConfiguration.buttonList,
            expression = calculatorUiState.expression,
            modifier = Modifier
                .weight(screenConfiguration.weightForKeyboard)
        )
    }
}

@Composable
fun CalculatorKeyboard(
    modifier: Modifier = Modifier,
    onSymbolClick: (CalcButton) -> Unit,
    onACClick: () -> Unit,
    onDELClick: (String) -> Unit,
    onEqualClick: (String) -> Unit,
    elements: List<CalcButton>,
    expression: String
) {


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
                        onClick = { onSymbolClick(temp) },
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
                onClick = { onACClick() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            CalculatorButton(
                symbol = "DEL",
                expression = expression,
                onClick = { onDELClick(expression) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val calcButton = CalcButton("PI", false, "PI")
                CalculatorButton(
                    symbol = "PI",
                    expression = expression,
                    onClick = { onSymbolClick(calcButton) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            CalculatorButton(
                symbol = "=",
                expression = expression,
                onClick = { onEqualClick(expression) },
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