package com.example.notify_app.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notify_app.R
import com.example.notify_app.ui.theme.NotifyappTheme


data class JournalCardData (
    @DrawableRes val img: Int,
    val title: String,
    val artist: String,
    val song: String
)

@Composable
fun JournalCard(data: JournalCardData){
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = data.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(16.dp))
            Column() {
                Text(text = data.title, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = data.song, style = MaterialTheme.typography.bodySmall)
                Text(text = data.artist, style = MaterialTheme.typography.bodySmall)
            }
        }

    }
}

// will require being plugged into Spotify API call
@Composable
fun songResult(data: JournalCardData){
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = data.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(16.dp))
            Column() {
                Text(text = data.title, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = data.artist, style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotifyappTheme {
        JournalCard(JournalCardData(R.drawable.placeholder, "Lorem Ipsum Let's get this done", "RadioHead", "Jigsaw Falling into Place"))
    }
}