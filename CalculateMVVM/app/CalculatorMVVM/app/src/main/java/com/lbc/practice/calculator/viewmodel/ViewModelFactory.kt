package com.lbc.practice.calculator.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbc.practice.calculator.data.resource.Repository
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(var repository: Repository, var application: Application, var calc: CalculateManager) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProblemViewModel::class.java)) {
            return ProblemViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(calc) as T
        }
        else run { throw IllegalArgumentException("ViewModel Not Found") }
    }

}