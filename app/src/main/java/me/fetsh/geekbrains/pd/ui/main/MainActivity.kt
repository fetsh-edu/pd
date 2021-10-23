package me.fetsh.geekbrains.pd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import klaster.Klaster
import me.fetsh.geekbrains.pd.Contract
import me.fetsh.geekbrains.pd.R
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.databinding.ActivityMainBinding
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.ui.BaseActivity

class MainActivity : BaseActivity<RemoteData>() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var listViewPresenter: Contract.ListViewPresenter

    override fun createPresenter(): Contract.Presenter<RemoteData, Contract.View> {
        return MainPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchFab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    presenter.getData(searchWord, true)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

        binding.mainActivityRecyclerview.layoutManager = LinearLayoutManager(applicationContext)

        val adapterWithLVP = createAdapter(layoutInflater) {}
        adapter = adapterWithLVP.first
        listViewPresenter = adapterWithLVP.second
        binding.mainActivityRecyclerview.adapter = adapter
    }

    private fun createAdapter(
        layoutInflater: LayoutInflater,
        onItemClick: (DataModel) -> Unit
    ): Pair<RecyclerView.Adapter<*>, Contract.ListViewPresenter> {
        var dataModels: List<DataModel> = emptyList()

        val adapter = Klaster.get()
            .itemCount { dataModels.size }
            .view(R.layout.item_word, layoutInflater)
            .bind { position ->
                val dataModel = dataModels[position]
                itemView.findViewById<TextView>(R.id.header_textview_recycler_item).text = dataModel.query
                itemView.findViewById<TextView>(R.id.description_textview_recycler_item).text =
                    dataModel.meanings.firstOrNull()?.translation?.text
                itemView.setOnClickListener { onItemClick(dataModel) }
            }
            .build()

        val listViewPresenter = object : Contract.ListViewPresenter {
            override fun setItems(items: List<DataModel>) {
                dataModels = items
                adapter.notifyDataSetChanged()
            }
        }

        return adapter to listViewPresenter
    }

    override fun renderData(remoteData: RemoteData) {
        when (remoteData) {
            is RemoteData.Success -> {
                val dataModels = remoteData.data
                if (dataModels.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    listViewPresenter.setItems(dataModels)
                }
            }
            is RemoteData.Loading,  -> {
                showViewLoading()
                if (remoteData.progress != null) {
                    binding.progressBarHorizontal.visibility = View.VISIBLE
                    binding.progressBarRound.visibility = View.GONE
                    binding.progressBarHorizontal.progress = remoteData.progress
                } else {
                    binding.progressBarHorizontal.visibility = View.GONE
                    binding.progressBarRound.visibility = View.VISIBLE
                }
            }
            is RemoteData.Error -> {
                showErrorScreen(remoteData.t.message)
            }
            RemoteData.Initial -> {
                showViewInitial()
            }
        }
    }

    private fun showViewInitial() {
        binding.initialTextview.text = getString(R.string.initial_message)
        binding.initialTextview.visibility = View.VISIBLE
        binding.mainActivityRecyclerview.visibility = View.GONE
        binding.successLinearLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLinearLayout.visibility = View.GONE
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            presenter.getData("hi", true)
        }
    }

    private fun showViewSuccess() {
        binding.initialTextview.visibility = View.GONE
        binding.successLinearLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLinearLayout.visibility = View.GONE
    }

    private fun showViewLoading() {
        binding.successLinearLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.VISIBLE
        binding.errorLinearLayout.visibility = View.GONE
    }

    private fun showViewError() {
        binding.successLinearLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLinearLayout.visibility = View.VISIBLE
    }

    companion object {

        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}