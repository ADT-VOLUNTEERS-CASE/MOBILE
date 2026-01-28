package org.adt.presentation.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.ivk1800.riflesso.Riflesso

@HiltAndroidApp
class Application: Application() {
    override fun onCreate() {
        Riflesso.initialize()
        super.onCreate()
    }
}