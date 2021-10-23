package me.fetsh.geekbrains.pd.ui.main


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.interactor.main.MainInteractor
import me.fetsh.geekbrains.pd.model.remote.RemoteRepository

class MainPresenter(
    private val interactor: MainInteractor = MainInteractor(
        RemoteRepository(),
        RemoteRepository()
    ),
) : Contract.Presenter<RemoteData, Contract.View> {

    private val compositeDisposable = CompositeDisposable()

    private var currentView: Contract.View? = null

    override fun attachView(view: Contract.View) {
        if (currentView != view) {
            currentView = view
//            currentView?.renderData(RemoteData.Initial)
        }
    }

    override fun detachView(view: Contract.View) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(interactor.getData(word, isOnline)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { currentView?.renderData(RemoteData.Loading(null)) }
            .subscribe { currentView?.renderData(it) }
        )
    }
}