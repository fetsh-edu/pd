package me.fetsh.geekbrains.pd

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import io.reactivex.Observable
import me.fetsh.geekbrains.pd.model.DataModel

sealed interface RemoteData {
    object Initial : RemoteData
    data class Success(val data: List<DataModel>) : RemoteData
    data class Error(val t: Throwable) : RemoteData
    data class Loading(val progress: Int? = null) : RemoteData
}

interface Contract {
    interface Interactor<T> {
        fun getData(word: String, isRemoteSource: Boolean): Observable<T>
    }

    interface Repository<T> {
        fun getData(word: String): Observable<Result<List<DataModel>, FuelError>>
    }
}
