package me.fetsh.geekbrains.pd.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.fetsh.geekbrains.pd.ui.main.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity
}