package me.fetsh.geekbrains.pd.interactor.main

import io.reactivex.Observable
import io.reactivex.Single
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.model.DataModel

class MainInteractor(
    private val remoteRepository: Contract.Repository<List<DataModel>>,
    private val localRepository: Contract.Repository<List<DataModel>>
) : Contract.Interactor<RemoteData> {

    override fun getData(word: String, isRemoteSource: Boolean): Observable<RemoteData> {
        return when {
            word.isBlank() -> {
                Single.just(RemoteData.Initial as RemoteData).toObservable()
            }
            isRemoteSource -> {
                remoteRepository.getData(word).map { result ->
                    result.fold(
                        { success -> RemoteData.Success(success) },
                        { failure -> RemoteData.Error(failure) }
                    )
                }
            }
            else -> {
                localRepository.getData(word).map { result ->
                    result.fold(
                        { success -> RemoteData.Success(success) },
                        { failure -> RemoteData.Error(failure) }
                    )
                }
            }
        }
    }
}