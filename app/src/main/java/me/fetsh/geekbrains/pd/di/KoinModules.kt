package me.fetsh.geekbrains.pd.di

import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.interactor.main.MainInteractor
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.model.remote.RemoteRepository
import me.fetsh.geekbrains.pd.ui.main.MainViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single<Contract.Repository<List<DataModel>>>(named(KEY_REMOTE)) {RemoteRepository()}
    single<Contract.Repository<List<DataModel>>>(named(KEY_LOCAL)) {RemoteRepository()}
}

val mainScreen = module {
    factory<Contract.Interactor<RemoteData>> {
        MainInteractor(
            remoteRepository = get(named(KEY_REMOTE)),
            localRepository = get(named(KEY_LOCAL))
        )
    }
    factory {
        MainViewModel(get())
    }
}