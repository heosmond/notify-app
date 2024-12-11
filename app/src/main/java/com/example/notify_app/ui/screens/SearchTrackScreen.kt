package com.example.notify_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notify_app.api.data.Track
import com.example.notify_app.composables.JournalCardData
import com.example.notify_app.events.NoteEvent
import com.example.notify_app.state.NoteState
import com.example.notify_app.viewmodel.NotesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@OptIn(FlowPreview::class)
@Composable
fun SearchTrackScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        var query by remember { mutableStateOf("") }
        LaunchedEffect(query) {
            snapshotFlow { query }
                .debounce(300)
                .filter { it.isNotBlank() }
                .collect { onEvent(NoteEvent.SearchTracks(it)) }
        }

        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search for a track") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        if (state.searchResults?.tracks?.tracks.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn {
                state.searchResults?.tracks?.let {
                    items(it.tracks) { track ->
                        TrackItem(
                            track = track,
                            onClick = { onEvent(NoteEvent.SelectTrack(track)); navController.navigate("AddNotesScreen") }
                        )
                    }
                }
            }
        }

        state.selectedTrack?.let { track ->
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Selected: ${track.name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick: () -> Unit){
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
//            Image(
//                painter = painterResource(id = data.img),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(track.name, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Text(track.artists[0].name , style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}