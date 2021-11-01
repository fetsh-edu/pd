package me.fetsh.geekbrains.pd.di

import dagger.Module
import dagger.Provides
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.interactor.main.MainInteractor
import me.fetsh.geekbrains.pd.model.DataModel
import javax.inject.Named
import javax.inject.Singleton

@Module
class InteractorModule {

    @Singleton
    @Provides
    fun providesInteractor(
        @Named(KEY_REMOTE) remoteRepository: Contract.Repository<List<DataModel>>,
        @Named(KEY_LOCAL) localRepository: Contract.Repository<List<DataModel>>
    ) : Contract.Interactor<RemoteData> = MainInteractor(
        remoteRepository = remoteRepository,
        localRepository = localRepository
    )
}