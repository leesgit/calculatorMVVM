package com.lbc.practice.calculator.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

class CalculatorViewModel : ViewModel {
    val text = MutableLiveData<String>()
    val result = MutableLiveData<String>()

    var statement = StringBuilder("0")
    var symbol = false
    var point = false
    var id = 0
    var calc : CalculateManager

    constructor(calc: CalculateManager?) : super() {
        this.calc = calc!!
    }


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
//                calculatorView?.Toast("입력값을 제대로 해주세요.")
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