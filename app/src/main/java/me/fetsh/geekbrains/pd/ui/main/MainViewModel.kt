package me.fetsh.geekbrains.pd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.model.DataModel

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel(
    private val interactor: Contract.Interactor<RemoteData>
) : ViewModel() {

    private val scope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
    )

    private val _query : MutableStateFlow<String> = MutableStateFlow("")
    val query : StateFlow<String> = _query

    private val _words : MutableStateFlow<RemoteData> = MutableStateFlow(RemoteData.Initial)
    val words : StateFlow<RemoteData> = _words

    private val _selectedWord : MutableLiveData<DataModel?> = MutableLiveData(null)
    val selectedWord : LiveData<DataModel?> = _selectedWord

    init {
        scope.launch {
            query
                .debounce(500)
                .onEach { _words.emit(RemoteData.Loading(0)) }
                .mapLatest { query ->
                    if (query.trim().isBlank())
                        RemoteData.Initial
                    else {
                        interactor.getData(query.trim(), true)
                    }
                }
                .collect { result -> _words.emit(result) }
        }
    }

    fun setQuery(text: String) {
        _query.value = text
    }

    fun selectWord(word: DataModel) {
        _selectedWord.value = word
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
