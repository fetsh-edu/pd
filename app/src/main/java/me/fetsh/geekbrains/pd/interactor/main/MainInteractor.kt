package me.fetsh.geekbrains.pd.interactor.main

import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.model.DataModel

class MainInteractor(
    private val remoteRepository: Contract.Repository<List<DataModel>>,
    private val localRepository: Contract.Repository<List<DataModel>>
) : Contract.Interactor<RemoteData> {

    override suspend fun getData(word: String, isRemoteSource: Boolean): RemoteData {
        return if (isRemoteSource) {
            remoteRepository.getData(word).fold(
                { success -> RemoteData.Success(success) },
                { failure -> RemoteData.Error(failure) }
            )
        }
        else {
            localRepository.getData(word).fold(
                { success -> RemoteData.Success(success) },
                { failure -> RemoteData.Error(failure) }
            )
        }
    }
}