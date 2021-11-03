package me.fetsh.geekbrains.pd

import android.app.Application
import me.fetsh.geekbrains.pd.di.application
import me.fetsh.geekbrains.pd.di.mainScreen
import org.koin.core.context.startKoin

class TranslatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen))
        }
    }
}