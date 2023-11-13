package com.anton.calculator.data

import org.mariuszgromada.math.mxparser.Expression


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
