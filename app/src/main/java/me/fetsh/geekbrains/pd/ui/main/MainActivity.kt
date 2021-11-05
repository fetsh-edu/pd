package me.fetsh.geekbrains.pd.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import me.fetsh.geekbrains.pd.RemoteData
import me.fetsh.geekbrains.pd.model.DataModel
import me.fetsh.geekbrains.pd.model.Meaning
import me.fetsh.geekbrains.pd.model.Translation
import me.fetsh.geekbrains.pd.ui.theme.DarkGray
import me.fetsh.geekbrains.pd.ui.theme.MyApplicationTheme
import me.fetsh.geekbrains.pd.ui.theme.TextMain
import me.fetsh.geekbrains.pd.ui.theme.Typography
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface {
                    SearchWordScreen(mainViewModel)
                }
            }
        }
    }
}



@Composable
fun WordItem(word: DataModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.value * density
                val y = size.height - strokeWidth / 2
                drawLine(
                    Color.LightGray,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
    ) {
        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(
                text = word.query,
                maxLines = 1,
                style = Typography.h3,
                color = DarkGray
            )
            Text(
                text = word.meanings.firstOrNull()?.translation?.text ?: "No translation",
                maxLines = 1,
                color = TextMain,

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordItemPreview() {
    val word = DataModel(
        "help",
        listOf(
            Meaning(
                translation = Translation(text = "Hello"),
                imageUrl = ""
            )
        )
    )
    WordItem(word)
}


@FlowPreview
@ExperimentalCoroutinesApi
@Composable
private fun SearchWordScreen(viewModel: MainViewModel) {
    val words : RemoteData by viewModel.words.collectAsState(RemoteData.Initial)
    val query : String by viewModel.query.collectAsState()
    Column {
        Surface(color = MaterialTheme.colors.primary, modifier = Modifier.fillMaxWidth()) {
            SearchInput(text = query, viewModel::setQuery)
        }
        when(words) {
            is RemoteData.Error -> {
                Text(text = (words as RemoteData.Error).t.message ?: "Error")
            }
            RemoteData.Initial -> {
                Text(text = "Not asked")
            }
            is RemoteData.Loading -> {
                Text(text = "Loading")
            }
            is RemoteData.Success -> {
                val words = words as RemoteData.Success
                if (words.data.isEmpty()){
                    Text(text = "Nothing is found")
                } else {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        words.data.map { dataModel ->
                            WordItem(word = dataModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchInput(text: String, onTextChange: (String) -> Unit) {
    Box(modifier = Modifier.padding(10.dp)) {
        SearchInputText(
            text = text,
            onTextChange = onTextChange,
            modifier =
            Modifier
                .background(MaterialTheme.colors.onPrimary)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, textColor = MaterialTheme.colors.primary),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier
    )
}