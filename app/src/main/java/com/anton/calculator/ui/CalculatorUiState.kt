package com.anton.calculator.ui

data class CalculatorUiState (
    val result: String = "",
    val expression: String = "",
    val history: List<String> = emptyList()
)