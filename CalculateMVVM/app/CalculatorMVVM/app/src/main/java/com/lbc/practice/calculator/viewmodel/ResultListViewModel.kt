package com.lbc.practice.calculator.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import com.lbc.practice.calculator.data.Result
import com.lbc.practice.calculator.data.resource.Repository

class ResultListViewModel(result: Result) : ViewModel() {
    val text  = result.num.toString()
    val state = result.isResult
}