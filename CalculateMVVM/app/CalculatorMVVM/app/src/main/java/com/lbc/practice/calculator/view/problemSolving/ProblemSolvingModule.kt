package com.lbc.practice.calculator.view.problemSolving

import android.arch.lifecycle.ViewModelProvider
import com.lbc.practice.calculator.di.ActivityScope
import com.lbc.practice.calculator.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class ProblemSolvingModule {

    @ActivityScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory) :ViewModelProvider.Factory

}
