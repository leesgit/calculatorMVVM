package com.lbc.practice.calculator.util

import dagger.Module
import dagger.Provides

@Module
class ManagerModule {

    @Provides
    fun provideCalculator() : CalculateManager {
        return CalculateManager()
    }

    @Provides
    fun provideMusic(): MusicManager {
        return MusicManager()
    }

}