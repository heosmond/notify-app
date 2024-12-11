package com.example.notify_app.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation


@Composable
fun DisplayImage(
    imageUrl: String?,
    localFallbackImage: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl) // The URL or data for the image
            .error(localFallbackImage) // Resource ID for the fallback image
            .placeholder(localFallbackImage) // placeholder image
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop, // Adjust as needed
        modifier = modifier
    )
}