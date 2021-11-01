package me.fetsh.geekbrains.pd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.interactor.main.MainInteractor
import me.fetsh.geekbrains.pd.model.remote.RemoteRepository

class MainViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val interactor: MainInteractor = MainInteractor(
        RemoteRepository(),
        RemoteRepository()
    )

    private val _words : MutableLiveData<RemoteData> = MutableLiveData(RemoteData.Initial)
    val words : LiveData<RemoteData> = _words

    private val _query : MutableLiveData<String> = MutableLiveData("")
    val query : LiveData<String> = _query

    fun setQuery(text: String) {
        if (text.isBlank()) {
            _words.postValue(RemoteData.Initial)
        } else if (_query.value != text ){
            search(text, true)
        }
        _query.value = text
    }

    private fun search(word: String, isOnline: Boolean) {
        compositeDisposable.add(interactor.getData(word, isOnline)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _words.postValue(RemoteData.Loading(null))
            }
            .subscribe {
                _words.postValue(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
