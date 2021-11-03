package me.fetsh.geekbrains.pd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData

class MainViewModel(
    private val interactor: Contract.Interactor<RemoteData>
) : ViewModel() {

    private val scope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
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
        scope.launch {
            _words.postValue(RemoteData.Loading(null))
            _words.postValue(interactor.getData(word, isOnline))
        }
    }
}
