package me.fetsh.geekbrains.pd

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.fetsh.geekbrains.pd.di.DaggerAppComponent
import javax.inject.Inject

class TranslatorApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(app = this)
            .build()
            .inject(app = this)
    }
}