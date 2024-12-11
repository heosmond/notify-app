package com.example.notify_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import com.example.notify_app.R
import com.example.notify_app.composables.DisplayImage
import com.example.notify_app.data.Note
import com.example.notify_app.events.NoteEvent
import com.example.notify_app.state.NoteState

@Composable
fun NotesScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                IconButton(onClick = { onEvent(NoteEvent.SortNotes) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Sort,
                        contentDescription = "Sort Notes",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("SearchTrackScreen")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add new note"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.notes.size) { index ->
                NoteCard(
                    note = state.notes[index],
                    onDelete = { onEvent(NoteEvent.DeleteNote(state.notes[index])) },
                    onClick = { navController.navigate("ViewNoteScreen/${state.notes[index].id}") }
                )
            }
        }
    }
}



@Composable
fun NoteCard(
    note: Note,
    onDelete: () -> Unit,
    onClick: () -> Unit
){
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
            .clickable( onClick = { onClick() }),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            DisplayImage(
                imageUrl = note.imagePath,
                localFallbackImage = R.drawable.placeholder,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = note.song,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = note.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.End)
                        .absoluteOffset(x = (-2).dp, y = (-2).dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Note",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

        }


    }
}

//@Preview
//@Composable
//fun NotesScreenPreview() {
//    NoteCard(
//        note = Note(
//            title = "heelo",
//            song = "song",
//            artist = "artist",
//            content = "content",
//            genre = "wuw",
//            imagePath = "https://i.scdn.co/image/ab67616d000048518b72243c4bf2544e00183e1d",
//            lastModified = System.currentTimeMillis(),
//            year = "year"
//        ),
//        onDelete = {}
//
//    )
//}

