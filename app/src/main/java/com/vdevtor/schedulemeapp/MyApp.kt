package com.vdevtor.schedulemeapp

import android.app.Application
import com.vdevtor.schedulemeapp.di.scheduleMeModules
import com.vdevtor.schedulemeapp.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApp)
            modules(scheduleMeModules, viewModelModules)
        }
    }
}