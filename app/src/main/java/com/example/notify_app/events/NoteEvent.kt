package com.example.notify_app.events

import com.example.notify_app.api.data.Track
import com.example.notify_app.data.Note

sealed interface NoteEvent {
    object SortNotes: NoteEvent
    object SaveNote: NoteEvent

    data class DeleteNote(val note: Note): NoteEvent


    data class SetTitle(val title: String): NoteEvent
//    data class SetSong(val song: String): NoteEvent
//    data class SetArtist(val artist: String): NoteEvent
//    data class SetYear(val year: String): NoteEvent
//    data class SetGenre(val genre: String): NoteEvent
    data class SetContent(val content: String): NoteEvent

    data class SelectTrack(val track: Track) : NoteEvent

    data class SearchTracks(val query: String) : NoteEvent

    //not yet implemented
//    data class SetSearchQuery(val query: String): NoteEvent
}