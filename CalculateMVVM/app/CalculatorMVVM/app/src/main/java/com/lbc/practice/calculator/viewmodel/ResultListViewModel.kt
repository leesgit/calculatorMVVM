package com.lbc.practice.calculator.viewmodel


import androidx.lifecycle.ViewModel
import com.lbc.practice.calculator.data.Result

class ResultListViewModel(result: Result) : ViewModel() {
    val text  = result.num.toString()
    val state = result.isResult
}