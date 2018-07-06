package com.lbc.practice.calculator.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ApplicationModule {

    @Binds
    abstract fun bindContext(application: Application): Context


}
