package com.anton.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anton.calculator.models.CalcButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mariuszgromada.math.mxparser.Expression

class CalculatorViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()


    var history: MutableList<String> = mutableListOf()

    var expression by mutableStateOf("")
        private set

    private var prevElementIsDot by mutableStateOf(false)


    private var prevElementIsFunList by mutableStateOf(mutableListOf(false))


    init {
        resetCalculator()
    }

    fun onDefaultButtonClick(
        calcButton: CalcButton
    ) {
        if ("." == calcButton.symbol && prevElementIsDot.not()) {
            expression += calcButton.element
            prevElementIsDot = true
            prevElementIsFunList.add(calcButton.isFunction)
        } else if (
            (prevElementIsFunList.last().not() || calcButton.isFunction.not()) &&
            ("." == calcButton.symbol).not()
        ) {
            expression += calcButton.element
            if (calcButton.isFunction) {
                prevElementIsDot = false
            }
            prevElementIsFunList.add(calcButton.isFunction)
        }
    }

    fun onACButtonClick() {
        prevElementIsFunList = mutableListOf(false)
        prevElementIsDot = false
        expression = ""
        _uiState.update {
            it.copy(
                result = ""
            )
        }
    }


    private fun solveExpression(expression: String): String {
        val answer: String
        try {
            answer = Expression(
                expression
                    .replace("÷", "/")
                    .replace("×", "*")
                    .replace("log", "log10")
                    .replace("PI", "pi")

            ).calculate().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid expression"
        }
        return answer
    }

    fun onRemoveButtonClick() {
        if (prevElementIsFunList.size > 1) {
            if (prevElementIsFunList.last()) {
                prevElementIsDot = true
            }
            prevElementIsFunList.removeLast()
        }

        if (expression.isNotEmpty()) {
            expression = expression.substring(0, expression.length - 1)
        }
    }

    fun onEqualButtonClick() {
        if (expression.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    result = solveExpression(expression)
                )
            }
            history.add("$expression = ${_uiState.value.result}")
            if (history.size > 3) {
                history.removeFirst()
            }
        }
    }

    private fun resetCalculator() {
        prevElementIsFunList.clear()
        prevElementIsFunList.add(false)
        prevElementIsDot = false
        expression = ""
        _uiState.update {
            it.copy(
                result = ""
            )
        }
    }
}