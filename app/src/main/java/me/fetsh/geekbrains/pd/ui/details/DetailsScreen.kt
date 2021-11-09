package me.fetsh.geekbrains.pd.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import me.fetsh.geekbrains.pd.R
import me.fetsh.geekbrains.pd.model.DataModel

@Composable
fun DetailsScreen(word: DataModel?) {
    word?.let { word ->
        Column() {
            DetailsText(header = word.query, description = word.translation() ?: "")
            DetailsImage(word.imageUrl())
        }
    }
}
@Composable
fun DetailsText(header : String, description: String) {
    Column() {
        Text(header)
        Text(description)
    }
}

@Composable
fun DetailsImage(imageUrl: String?) {
    if (!imageUrl.isNullOrBlank()) {
        GlideImage(
            imageModel = imageUrl,
            modifier = Modifier.fillMaxSize(),
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Crop,
            // shows an image with a circular revealed animation.
            circularReveal = CircularReveal(duration = 250),
            // shows a placeholder ImageBitmap when loading.
            placeHolder = painterResource(id = R.drawable.ic_launcher_background),
            // shows an error ImageBitmap when the request failed.
            error = painterResource(R.drawable.ic_launcher_foreground)
        )
    }
}

