package com.anton.calculator.ui

data class CalculatorUiState (
    val expression: String = "",
    val result: String = "",
    val history: List<String> = mutableListOf()
)