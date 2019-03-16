package com.lbc.practice.calculator.viewmodel


import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lbc.practice.calculator.util.CalculateManager
import java.lang.StringBuilder
import java.util.*

class CalculatorViewModel(calculator: CalculateManager) : ViewModel() {
    val text = MutableLiveData<String>()
    val result = MutableLiveData<String>()

    var statement = StringBuilder("0")
    var symbol = false
    var point = false
    var calc = calculator


    fun btnNum(view: View) {
        view.tag
        val str = view.tag as String
        if (statement.toString().equals("0")) {
            statement = StringBuilder(str)
        } else {
            statement = statement.append(str)
        }
        text.postValue(statement.toString())
        symbol = true
        point = true
    }

    fun btnSymbol(view: View) {

        val num = StringTokenizer(statement.toString(), "+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")
        if (symbol == true && num.countTokens() >= oper.countTokens()) {
            when (view.tag as String) {
                "plus" -> statement.append("+")
                "minus" -> statement.append("-")
                "division" -> statement.append("/")
                "multiply" -> statement.append("X")
            }
            text.postValue(statement.toString())
            symbol = false
        }
    }

    fun btnOption(view: View) {

        when (view.tag as String) {
            "back" -> if (statement.toString().length != 0) {
                statement.setLength(statement.length - 1)
                text.postValue(statement.toString())
                symbol = true
            }
            "clear" -> {
                statement = StringBuilder("0")
                text.postValue(statement.toString())
                result.postValue("")
            }
            "result"-> try {
                result.postValue(calc.calculate(statement.toString()))
            } catch (e: NumberFormatException) {
                Toast.makeText(view.context, "입력값을 제대로 해주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun btnPoint() {
        val decimalPoint = StringTokenizer(statement.toString(), "1234567890+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")

        if (point == true && oper.countTokens() == decimalPoint.countTokens()) {
            statement.append(".")
            text.postValue(statement.toString())
            point = false
        }
    }


}