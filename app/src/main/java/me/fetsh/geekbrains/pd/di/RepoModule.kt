package me.fetsh.geekbrains.pd.di

import dagger.Module
import dagger.Provides
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.model.remote.RemoteRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepoModule {
    @Provides
    @Singleton
    @Named(KEY_LOCAL)
    fun provideLocalRepo() : Contract.Repository<List<DataModel>> {
        return RemoteRepository()
    }
    @Provides
    @Singleton
    @Named(KEY_REMOTE)
    fun provideRemoteRepo() : Contract.Repository<List<DataModel>> {
        return RemoteRepository()
    }
}