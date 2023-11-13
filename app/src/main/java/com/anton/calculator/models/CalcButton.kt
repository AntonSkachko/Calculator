package com.anton.calculator.models

data class CalcButton (
    val symbol: String,
    val isFunction: Boolean,
    val element: String
)

//// new Button keyboard
//val portraitElements = listOf(
//    "1", "2", "3", "÷",
//    "4", "5", "6", "×",
//    "7", "8", "9", "-",
//    ".", "0", "e", "+"
//)
//
//val landscapeElements = listOf(
//    "1", "2", "3", "÷", "(", "SIN",
//    "4", "5", "6", "×", ")", "COS",
//    "7", "8", "9", "-", "^", "TG",
//    ".", "0", "%", "+", "sqrt", "LOG"
//)
//val endList = listOf("Del", "AC", "=", "%")
