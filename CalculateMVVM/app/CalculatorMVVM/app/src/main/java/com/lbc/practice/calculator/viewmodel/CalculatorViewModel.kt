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
    val rxText = MutableLiveData<String>()
    val numberSound = MutableLiveData<Boolean>()
    val numberSoundZero = MutableLiveData<Boolean>()

    var statement = StringBuilder("0")
    var symbol = false
    var point = false
    var calc = calculator

    var resourceids: IntArray? = IntArray(10)
    var resouceCal: Int = 0

    fun btnNum(view: View) {
        view.tag
        val str = view.tag as String
        statement = if (statement.toString().equals("0")) {
            StringBuilder(str)
        } else {
            statement.append(str)
        }
        text.postValue(statement.toString())
        symbol = true
        point = true
    }

    fun btnSymbol(view: View) {
        val num = StringTokenizer(statement.toString(), "+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")
        if (symbol && num.countTokens() >= oper.countTokens()) {
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
            "back" -> if (statement.toString().isNotEmpty()) {
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

        if (point && oper.countTokens() == decimalPoint.countTokens()) {
            statement.append(".")
            text.postValue(statement.toString())
            point = false
        }
    }

    fun textChange() {
        text.value?.let {
            when (it[text.value!!.length - 1]) {
                '+', '-', 'X', '/' ->
                    rxText.value = calc.calculate(it.substring(0, it.length - 1))
                '1', '2', '3', '4', '5', '6', '7', '8', '9' ->
                    numberSound.value = true
                '0' -> if (it.length > 1) {
                    numberSoundZero.value = true
                }
            }
        }
    }


}