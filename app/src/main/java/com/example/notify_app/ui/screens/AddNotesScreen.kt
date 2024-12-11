package com.example.notify_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notify_app.events.NoteEvent
import com.example.notify_app.state.NoteState

@Composable
fun AddNotesScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(NoteEvent.SaveNote)
                    navController.navigate("NotesScreen")
              },
            ){
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Save Note")
            }
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                value = state.title,
                onValueChange = {
                    onEvent(NoteEvent.SetTitle(it))
                },
                placeholder = {
                    Text(text = "Title")
                }
            )
            TextField(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                value = state.content,
                onValueChange = {
                    onEvent(NoteEvent.SetContent(it))
                },
                placeholder = {
                    Text(text = "Type your note here...")
                }
            )
        }

    }

}
