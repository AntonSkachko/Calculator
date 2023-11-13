package com.anton.calculator.data

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.anton.calculator.models.CalcButton

class Datasource {
    private val portraitButtonList = listOf<CalcButton>(
        CalcButton("1", false, "1"),
        CalcButton("2", false, "2"),
        CalcButton("3", false, "3"),
        CalcButton("÷", true, "÷"),

        CalcButton("4", false, "4"),
        CalcButton("5", false, "5"),
        CalcButton("6", false, "6"),
        CalcButton("×", true, "×"),

        CalcButton("7", false, "7"),
        CalcButton("8", false, "8"),
        CalcButton("9", false, "9"),
        CalcButton("-", true, "-"),

        CalcButton(".", false, "."),
        CalcButton("0", false, "0"),
        CalcButton("%", true, "%"),
        CalcButton("+", true, "+")
    )

    private val landscapeButtonList = listOf<CalcButton>(
        CalcButton("1", false, "1"),
        CalcButton("2", false, "2"),
        CalcButton("3", false, "3"),
        CalcButton("÷", true, "÷"),
        CalcButton("(", false, "("),
        CalcButton("sin", false, "sin("),

        CalcButton("4", false, "4"),
        CalcButton("5", false, "5"),
        CalcButton("6", false, "6"),
        CalcButton("×", true, "×"),
        CalcButton(")", false, ")"),
        CalcButton("cos", false, "cos("),

        CalcButton("7", false, "7"),
        CalcButton("8", false, "8"),
        CalcButton("9", false, "9"),
        CalcButton("-", true, "-"),
        CalcButton("^", true, "^"),
        CalcButton("tg", false, "tg("),

        CalcButton(".", false, "."),
        CalcButton("0", false, "0"),
        CalcButton("%", true, "%"),
        CalcButton("+", true, "+"),
        CalcButton("e", false, "e"),
        CalcButton("log", false, "log(")
    )

    @Composable
    fun takeButtonList(): List<CalcButton> {
        return if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Datasource().portraitButtonList
        } else {
            Datasource().landscapeButtonList
        }
    }

    @Composable
    fun takeWeightForScreen(): Float {
        return if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            0.5f
        } else {
            0.3f
        }
    }

    @Composable
    fun takeWeightForKeyboard(): Float {
        return if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            0.5f
        } else {
            0.7f
        }
    }

}