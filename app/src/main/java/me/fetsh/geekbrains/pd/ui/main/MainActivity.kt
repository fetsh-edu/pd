package me.fetsh.geekbrains.pd.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.ui.TranslatorScreen
import me.fetsh.geekbrains.pd.ui.details.DetailsScreen
import me.fetsh.geekbrains.pd.ui.search.SearchWordScreen
import me.fetsh.geekbrains.pd.ui.theme.MyApplicationTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatorApp(mainViewModel)
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
@Composable
fun TranslatorApp(viewModel: MainViewModel) {
    MyApplicationTheme {
        val navController = rememberNavController()
        val selectedWord : DataModel? by viewModel.selectedWord.observeAsState(null)
        val navigationIcon: (@Composable () -> Unit)? =
            if (navController.previousBackStackEntry != null) {
                {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            } else {
                null
            }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text("SkyEng Dictionary")},
                    navigationIcon = navigationIcon
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TranslatorScreen.Search.name,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(TranslatorScreen.Search.name) {
                    SearchWordScreen(
                        viewModel = viewModel,
                        onWordClick = { word ->
                            viewModel.selectWord(word)
                            navController.navigate(TranslatorScreen.Details.name)
                        }
                    )
                }
                composable(TranslatorScreen.Details.name) {
                    DetailsScreen(word = selectedWord)
                }
            }
        }
    }
}