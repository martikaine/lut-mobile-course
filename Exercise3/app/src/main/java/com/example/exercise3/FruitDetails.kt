package com.example.exercise3

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun FruitDetails(index: Int) {
    val config = LocalConfiguration.current
    val resources = LocalContext.current.resources
    val density = LocalDensity.current
    val screenWidthPx = with(density) { config.screenWidthDp.dp.roundToPx() }

    Image(
        bitmap = getScaledBitmap(resources, getImageId(index), screenWidthPx),
        contentDescription = "An image of the selected fruit.",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Fit
    )
}

fun getImageId(index: Int): Int {
    return when (index) {
        0 -> R.drawable.peach
        1 -> R.drawable.tomato
        2 -> R.drawable.squash
        else -> -1
    }
}

fun getScaledBitmap(res: Resources, imageId: Int, screenWidthPx: Int): ImageBitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, imageId, options)

    var imgWidth = options.outWidth
    if (imgWidth > screenWidthPx) {
        options.inSampleSize = (imgWidth.toFloat() / screenWidthPx.toFloat()).roundToInt()
    }

    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, imageId, options).asImageBitmap()
}