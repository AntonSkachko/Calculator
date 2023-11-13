package com.anton.calculator.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mariuszgromada.math.mxparser.Expression

class CalculatorViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()


    fun solveExpression(expression: String): String {
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

    fun removeElement(expression: String): String {
        return if (expression.isNotEmpty()) {
            expression.substring(0, expression.length - 1)
        } else {
            expression
        }
    }
}