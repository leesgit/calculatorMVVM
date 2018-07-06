package com.lbc.practice.calculator.viewmodel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.lbc.practice.calculator.data.resource.Repository
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory : ViewModelProvider.Factory{

    var repository: Repository
    var application : Application
    var calc : CalculateManager


    @Inject
    constructor(repository: Repository, application: Application, calc : CalculateManager) {
        this.repository = repository
        this.application = application
        this.calc = calc
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProblemViewModel::class.java)) {
            return ProblemViewModel(repository, application) as T
        } else if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(calc) as T
        }
        else run { throw IllegalArgumentException("ViewModel Not Found") }
    }

}