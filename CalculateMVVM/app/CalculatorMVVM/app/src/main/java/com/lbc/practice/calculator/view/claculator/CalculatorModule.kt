package com.lbc.practice.calculator.view.claculator

import androidx.lifecycle.ViewModelProvider
import com.lbc.practice.calculator.di.ActivityScope
import com.lbc.practice.calculator.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class CalculatorModule {
    @ActivityScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

}