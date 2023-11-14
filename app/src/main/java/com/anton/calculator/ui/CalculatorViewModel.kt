package com.anton.calculator.ui

import android.content.res.Configuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anton.calculator.data.Datasource
import com.anton.calculator.models.CalcButton
import com.anton.calculator.utils.ScreenConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mariuszgromada.math.mxparser.Expression

class CalculatorViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()


    private var prevElementIsDot by mutableStateOf(false)
    private var prevElementIsFunList by mutableStateOf(mutableListOf(false))


    fun onDefaultButtonClick(calcButton: CalcButton) {
        val updatedExpression = buildUpdatedExpression(calcButton)
        updateUiState(_uiState.value.copy(expression = updatedExpression))
    }

    fun onACButtonClick() {
        val newState = CalculatorUiState()
        val expression = _uiState.value.expression
        updateUiState(newState)

        if (expression.takeLast(1) == ".") {
            prevElementIsDot = false
        }
    }


    fun onRemoveButtonClick() {

        val expression = _uiState.value.expression

        if (expression.isNotEmpty()) {
            if (prevElementIsFunList.size > 1) {
                if (prevElementIsFunList.last()) {
                    prevElementIsDot = true
                }
                prevElementIsFunList.removeLast()
            }

            updateUiState(
                _uiState.value.copy(
                    expression = expression.dropLast(1)
                )
            )

            if (expression.takeLast(1) == ".") {
                prevElementIsDot = false
            }
        }
    }

    fun onEqualButtonClick() {

        val result = solveExpression(_uiState.value.expression)

        val updatedHistory = _uiState.value.history.toMutableList().apply {
            add("${_uiState.value.expression} = $result")
            if (size > 3) {
                removeFirst()
            }
        }
        updateUiState(
            _uiState.value.copy(
                result = result,
                history = updatedHistory
            )
        )
    }

    fun getScreenConfiguration(orientation: Int): ScreenConfiguration {
        val buttonList: List<CalcButton>
        val weightForScreen: Float
        val weightForKeyboard: Float

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            buttonList = Datasource().portraitButtonList
            weightForScreen = 0.5f
            weightForKeyboard = 0.5f
        } else {
            buttonList = Datasource().landscapeButtonList
            weightForScreen = 0.3f
            weightForKeyboard = 0.7f
        }

        return ScreenConfiguration(buttonList, weightForScreen, weightForKeyboard)
    }

    private fun buildUpdatedExpression(calcButton: CalcButton): String {
        val expression = _uiState.value.expression

        if ("." == calcButton.symbol) {
            if (prevElementIsDot.not() &&
                prevElementIsFunList.isNotEmpty() &&
                !prevElementIsFunList.last()
            ) {
                prevElementIsDot = true
                prevElementIsFunList.add(calcButton.isFunction)
                return "$expression${calcButton.element}"
            }
        } else {
            if (prevElementIsFunList.last().not() || calcButton.isFunction.not()) {
                if (prevElementIsFunList.isNotEmpty() && prevElementIsFunList.last()) {
                    prevElementIsDot = false
                }
                prevElementIsFunList.add(calcButton.isFunction)
                return "$expression${calcButton.element}"
            }
        }

        return expression
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

    private fun updateUiState(newState: CalculatorUiState) {
        _uiState.value = newState
    }

}
