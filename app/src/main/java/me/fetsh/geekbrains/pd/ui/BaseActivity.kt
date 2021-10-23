package me.fetsh.geekbrains.pd.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.RemoteData

abstract class BaseActivity<T : RemoteData> : AppCompatActivity(), Contract.View {

    protected lateinit var presenter: Contract.Presenter<T, Contract.View>

    protected abstract fun createPresenter(): Contract.Presenter<T, Contract.View>

    abstract override fun renderData(data: RemoteData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }
}