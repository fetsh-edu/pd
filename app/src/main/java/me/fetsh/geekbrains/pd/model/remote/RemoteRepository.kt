package me.fetsh.geekbrains.pd.model.remote

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.forge.forgesDeserializerOf
import com.github.kittinunf.fuel.rx.rxObject
import com.github.kittinunf.result.Result
import io.reactivex.Observable
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.model.dataModelDeserializer

class RemoteRepository : Contract.Repository<List<DataModel>> {
    override fun getData(word: String): Observable<Result<List<DataModel>, FuelError>> =
        Fuel.get(
            "https://dictionary.skyeng.ru/api/public/v1/words/search",
            listOf("search" to word))
            .rxObject(forgesDeserializerOf(dataModelDeserializer))
            .toObservable()
}