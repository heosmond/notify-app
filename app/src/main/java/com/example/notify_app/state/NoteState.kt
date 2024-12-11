package com.example.notify_app.state

import com.example.notify_app.api.data.SearchResponse
import com.example.notify_app.api.data.Track
import com.example.notify_app.data.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val song: String = "",
    val artist: String = "",
    val year: String = "",
    val genre: String = "",
    val content: String = "",
    val imagePath: String = "",
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val editingNote: Note? = null,
    val selectedTrack: Track? = null, // Add selectedTrack field
    val searchResults: SearchResponse? = null

)