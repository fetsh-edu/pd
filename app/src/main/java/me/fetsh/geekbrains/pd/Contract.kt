package me.fetsh.geekbrains.pd

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import me.fetsh.geekbrains.pd.model.DataModel

sealed interface RemoteData {
    object Initial : RemoteData
    data class Success(val data: List<DataModel>) : RemoteData
    data class Error(val t: Throwable) : RemoteData
    data class Loading(val progress: Int? = null) : RemoteData
}

interface Contract {
    interface Interactor<T> {
        suspend fun getData(word: String, isRemoteSource: Boolean): T
    }

    interface Repository<T> {
        suspend fun getData(word: String): Result<T, FuelError>
    }
}
