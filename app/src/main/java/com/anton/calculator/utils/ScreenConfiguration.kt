package com.anton.calculator.utils

import com.anton.calculator.models.CalcButton

data class ScreenConfiguration(
    val buttonList: List<CalcButton>,
    val weightForScreen: Float,
    val weightForKeyboard: Float
)
