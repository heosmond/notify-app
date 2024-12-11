package com.example.notify_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify_app.api.SpotifyRepository
import com.example.notify_app.api.data.SearchResponse
import com.example.notify_app.api.data.Track
import com.example.notify_app.data.Note
import com.example.notify_app.data.NoteDao
import com.example.notify_app.events.NoteEvent
import com.example.notify_app.state.NoteState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.notify_app.api.data.Tracks


/* !!ViewModel handles events and data changes!!
* TODO: Consider approach to connecting data gotten from API to the
*  Dao fields, I think we just need to plug the values into here
*  !!research data caching methods with room to see how this is done*/

class NotesViewModel(private val dao: NoteDao) : ViewModel() {
    // DATABASE
    //like a get request but for notes from DB
    private val isSortedByDate = MutableStateFlow(true)

    //flip how notes are sorted, I think there's a better way to do this
    //Dunno why this needs stateIn
    @OptIn(ExperimentalCoroutinesApi::class)
    private var notes = isSortedByDate.flatMapLatest { sort ->
        if (sort) {
            dao.getNotesOrderedByLastModified()
        } else {
            dao.getNotesOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //get state object
    val _state = MutableStateFlow(NoteState())


    val state = combine(_state, notes, isSortedByDate) { state, notes, isSortedByDate ->
        state.copy(
            notes = notes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    // API
    private val repository = SpotifyRepository()

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: MutableLiveData<String?> get() = _accessToken

    fun getAccessToken() {
        viewModelScope.launch {
            val token = repository.fetchAccessToken()
            _accessToken.postValue(token)
        }
    }
    fun searchTracks(query: String) {
        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    _state.update {
                        it.copy(searchResults = SearchResponse(tracks = Tracks(tracks = emptyList())))
                    }
                    return@launch
                }

                Log.d("SearchTracks", "Searching for tracks with query: $query")

                val response = repository.getSearchResults(query)

                Log.d("SearchTracks", "Raw API Response: $response")

                if (response != null) {
                    Log.d("SearchTracks", "Found ${response.tracks.tracks.size} tracks.")
                    _state.update {
                        it.copy(searchResults = response) // Update state with the API response
                    }
                } else {
                    // Handle case where no tracks were found or returned
                    Log.d("SearchTracks", "No tracks found for query: $query")
                    _state.update {
                        it.copy(searchResults = SearchResponse(tracks = Tracks(tracks = emptyList())))
                    }
                }
            } catch (e: Exception) {
                // Handle error (e.g., network failure)
                Log.e("SearchTracks", "Error occurred during search: ${e.message}", e)
                _state.update {
                    it.copy(searchResults = SearchResponse(tracks = Tracks(tracks = emptyList())))
                }
            }
        }
    }
    fun getArtistNames(track: Track): String {
        return track.artists.joinToString(", ") { it.name }
    }


    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NoteEvent.SaveNote -> {
                //process: create new note object, use object to update DB, update state (effects UI)
                val title = state.value.title
                val content = state.value.content
                val song = state.value.song
                val artist = state.value.artist
                val year = state.value.year
                val imagePath = state.value.imagePath

                //No blank fields
                if (title.isBlank() || content.isBlank()) {
                    return
                }

                val note = Note(
                    title = title,
                    song = song,
                    artist = artist,
                    year = year,
                    genre = "unknown",//todo remove
                    content = content,
                    imagePath = imagePath, //todo save image url - req different rendering method
                    lastModified = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    state.value.id = dao.upsertNote(note)
                }
                //resets state after saving note
                _state.update {
                    it.copy(
                        title = "",
                        song = "",
                        artist = "",
                        year = "",
                        genre = "",
                        content = "",
                        imagePath = "",
                        id = 0
                    )
                }
            }
            is NoteEvent.SetContent -> {
                _state.update {
                    it.copy(
                        content = event.content
                    )
                }
            }
            is NoteEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            is NoteEvent.SelectTrack -> {
                _state.update {
                    it.copy(selectedTrack = event.track,
                        song = event.track.name,
                        artist = getArtistNames(event.track),
                        year = event.track.album.release_date,
                        imagePath = event.track.album.images[1].url)//300x300 album cover
                }
            }
            is NoteEvent.SearchTracks -> {
                searchTracks(event.query)
            }
            //flip-flop how notes are sorted?
            NoteEvent.SortNotes -> {
                isSortedByDate.value = !isSortedByDate.value
            }
        }
    }

}